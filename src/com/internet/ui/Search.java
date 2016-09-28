package com.internet.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.internet.db.BrcodeMessage;
import com.internet.db.DBTool;
import com.internet.db.MessageItem;
import com.internet.intrface.TopBarClickListener;
import com.internet.myui.TopBar;
import com.internet.netget.R;
import com.internet.service.WatchService;
import com.internet.tools.GetSDImage;
import com.internet.tools.ImageUtil;
import com.internet.tools.MessageSender;
import com.internet.tools.NormalUtil;
import com.internet.tools.UserSession;

public class Search extends Activity implements SurfaceHolder.Callback {

	private boolean hasSurface = false;
	private boolean playBeep = true;

	public static int width = 0;// 二维码扫描区宽度
	public static int height = 0;// 二维码扫描区高度
	private static final float BEEP_VOLUME = 0.80f;
	private static final long VIBRATE_DURATION = 200L;

	private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES;

	static {
		DISPLAYABLE_METADATA_TYPES = new HashSet<ResultMetadataType>(5);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.ISSUE_NUMBER);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.SUGGESTED_PRICE);
		DISPLAYABLE_METADATA_TYPES
				.add(ResultMetadataType.ERROR_CORRECTION_LEVEL);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.POSSIBLE_COUNTRY);
	}

	private enum Source {
		NATIVE_APP_INTENT, PRODUCT_SEARCH_LINK, ZXING_LINK, NONE
	}

	private CaptureActivityHandler handler = null;
	private ViewfinderView viewfinderView = null;
	private MediaPlayer mediaPlayer = null;

	private Source source = null;
	private String returnUrlTemplate = null;
	private Vector<BarcodeFormat> decodeFormats = null;
	private String characterSet = null;
	private InactivityTimer inactivityTimer = null;
	private FrameLayout frameLayout = null;
	private SurfaceView surfaceView = null;
	private TextView tip = null;
	private String lastContent;

	private int total, current;
	private char tag;
	private String waitSend = "";

	private boolean flashLightState = false;

	private ImageView image0, image1, image2;

	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				doCloseWaitDialog();
				break;
			case 1:
				doShowWaitDialog((String) msg.obj);
				break;
			case 2:
				BrcodeMessage item = (BrcodeMessage) msg.obj;
				String content = item.getContent();
				System.out.println(content);
				if (lastContent != null && lastContent.equals(content)) {
					lastContent = content.toString();
					resetPreview();
					System.out.println("重复扫描");
				} else {
					lastContent = content.toString();
					content = decode(content);
					if (content != null && content.contains("*")
							&& content.contains("/")) {
						String[] array1 = content.split("\\*");
						String biaoji = array1[array1.length - 1];
						System.out.println(biaoji);
						if (biaoji != null && biaoji.length() >= 3
								&& biaoji.charAt(1) == '/') { // 判断是否包含页码标签
							System.out.println("进入页码模式");
							int n = 0, N = 0;
							char c = 0;
							try {
								n = Integer.valueOf(biaoji.charAt(0) + "");
								N = Integer.valueOf(biaoji.charAt(2) + "");
								if (biaoji.length() == 4)
									c = biaoji.charAt(3);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							if (n != 0 && N != 0 && n <= N) { // 判断內容是否包含页码标签
								if (current == 0 && total == 0) {
									if (n == 1) {
										System.out.println("扫描为第一页");
										String str = "<font color='red'>第</font>"
												+ "<font color='blue'>"
												+ n
												+ "</font>"
												+ "<font color= 'red'>／"
												+ N
												+ "张扫描成功，请继续按顺序扫描！</font>";
										tip.setText(Html.fromHtml(str));
										playBeepSoundAndVibrate();
										current = n;
										total = N;
										tag = c;
										for (int i = 0; i < array1.length - 1; i++) {
											waitSend += array1[i];
										}
									} else {
										System.out.println("扫描顺序错误");
										tip.setText("失败！请按编号顺序重新扫描！");
										NormalUtil.displayMessage(
												getApplicationContext(),
												"失败！请按编号顺序重新扫描！");
									}
									resetPreview();
								} else if (N == total && c == tag) {// 判断获取的页码总数是否跟之前获取的一致
									if (n == current + 1) { // 判断当前页码是否为正确的顺序

										if (n == N) { // 当前扫描页码为最后一页
											System.out.println("扫描为最后一页");

											playBeepSoundAndVibrate();
											current = n;
											total = N;
											for (int i = 0; i < array1.length - 1; i++) {
												waitSend += array1[i];
											}
											String str = "<font color='red'>共</font>"
													+ "<font color='blue'>"
													+ N
													+ "</font>"
													+ "<font color= 'red'>张全部扫描成功！</font>";
											tip.setText(Html.fromHtml(str));
											if (waitSend != null
													&& waitSend.contains("##")) {

												waitSend += "*";
												String[] array2 = waitSend
														.split("##");
												// tip.setText(array2[0]);
												for (int i = 1; i < (array2.length - 1); i++) {
													if (array2[i].length() == 11) {
														System.out
																.println(array2[i]);
														MessageSender
																.getInstance()
																.sendSms(
																		array2[i],
																		array2[0],
																		getApplicationContext(),
																		true);
													}
												}
												saveBitmap(item.getBarcode());
												Message message = Message
														.obtain();
												message.obj = array2[0];
												message.what = 3;
												this.sendMessageDelayed(
														message, 3000);
											}
										} else if (n < N) { // 当前扫描页码为正确的顺序，并且是结束前的某页
											System.out.println("扫描为第" + n
													+ "页，共" + N + "页");
											String str = "<font color='red'>第</font>"
													+ "<font color='blue'>"
													+ n
													+ "</font>"
													+ "<font color= 'red'>／"
													+ N
													+ "张扫描成功，请继续按顺序扫描！</font>";
											tip.setText(Html.fromHtml(str));
											playBeepSoundAndVibrate();
											current = n;
											total = N;
											for (int i = 0; i < array1.length - 1; i++) {
												waitSend += array1[i];
											}
											resetPreview();
										}
									} else { // 没有按页码顺序扫描
										System.out.println("扫描顺序错误");
										current = 0;
										total = 0;
										waitSend = "";
										resetPreview();
										tip.setText("失败！请按编号顺序重新扫描！");
										NormalUtil.displayMessage(
												getApplicationContext(),
												"失败！请按编号顺序重新扫描！");
									}
								} else if (N == total && c != tag) {
									current = 0;
									total = 0;
									waitSend = "";
									resetPreview();
									tip.setText("因组群错乱而扫码失败，请找准组群重新扫码！");
									NormalUtil.displayMessage(
											getApplicationContext(),
											"因组群错乱而扫码失败，请找准组群重新扫码！");
								} else
									resetPreview();
							} else
								resetPreview();
						} else
							resetPreview();

					} else if (content != null && content.contains("##")) {

						playBeepSoundAndVibrate();
						content += "*";
						String[] array = content.split("##");
						tip.setText("恭喜扫描成功！");
						for (int i = 1; i < (array.length - 1); i++) {
							if (array[i].length() == 11) {
								System.out.println(array[i]);
								MessageSender.getInstance()
										.sendSms(array[i], array[0],
												getApplicationContext(), true);
							}
						}
						saveBitmap(item.getBarcode());
						Message message = Message.obtain();
						message.obj = array[0];
						message.what = 3;
						this.sendMessageDelayed(message, 3000);

					} else {
						resetPreview();
					}
				}

				break;
			case 3:
				Intent intent = new Intent();
				intent.setClass(Search.this, OkAct.class);
				intent.putExtra("info", (String) msg.obj);
				Search.this.startActivity(intent);
				finish();
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.basesearch);

		frameLayout = (FrameLayout) findViewById(R.id.search_frame);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		tip = (TextView) findViewById(R.id.text_tip);

		tip.setText("正在扫码采集中……");
		image0 = (ImageView) findViewById(R.id.image0);
		image0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				flashLightState = !flashLightState;
				CameraManager.get().switchFlashLight(flashLightState);

			}
		});

		image1 = (ImageView) findViewById(R.id.image1);
		image1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(Search.this, AlterSetting.class);
				Search.this.startActivity(intent);

			}
		});
		image2 = (ImageView) findViewById(R.id.image2);
		image2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				List<String> list = GetSDImage.getImgPathList(NormalUtil
						.getRootDir());
				if (list.size() == 0)
					NormalUtil.displayMessage(getApplicationContext(),
							"暂无保存图片可以查看");
				else {
					Intent intent = new Intent();
					intent.setClass(Search.this, PhotoPreview.class);
					intent.putExtra("path", NormalUtil.getRootDir());
					Search.this.startActivity(intent);
				}

			}
		});
		init();

		WatchService.actionReschedule(getApplicationContext());
		DBTool.getInstance().clearTimeout(getApplicationContext());
		if (UserSession.isFirst(this)) {
			NormalUtil.deletePath();
			UserSession.setFirstFalse(this);
			DBTool.getInstance().deleteAll(this);
		}

	}

	/**
	 * 二维码处理成功
	 * 
	 * @param rawResult
	 * @param barcode
	 */
	private void handleDecodeInternally(Result rawResult, Bitmap barcode) {
		viewfinderView.setVisibility(View.GONE);
		String content = rawResult.getText();
		resetStatusView();
		// showWaitDialog(content);
		BrcodeMessage item = new BrcodeMessage();
		item.setBarcode(barcode);
		item.setContent(content);
		handler2.obtainMessage(2, item).sendToTarget();
	}

	public void init() {
		setTopBar();
		CameraManager.init(getApplication());
		handler = null;
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		ViewTreeObserver observer = frameLayout.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				height = frameLayout.getMeasuredHeight();
				width = frameLayout.getMeasuredWidth();
				return true;
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// 初始化扫描界面
		super.onResume();
		resumeEvent();
	}

	public void resumeEvent() {
		resetStatusView();

		final SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		surfaceView.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!hasSurface) {
					initCamera(surfaceHolder);
					hasSurface = true;
				}
			}

		}, 500);
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}

		source = Source.NONE;
		decodeFormats = null;
		characterSet = null;
		initBeepSound();
	}

	@Override
	protected void onPause() {
		super.onPause();
		pauseEvent();
		// finish();
	}

	public void pauseEvent() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		if (flashLightState) {
			flashLightState = !flashLightState;
			CameraManager.get().switchFlashLight(flashLightState);
		}
		CameraManager.get().closeDriver();
		hasSurface = false;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		inactivityTimer.shutdown();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	/**
	 * 成功获取二维码，在handler中调用这个方法，回传bitmap
	 * 
	 * @param rawResult
	 * @param barcode
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		// lastResult = rawResult;
		if (barcode == null) {
			handleDecodeInternally(rawResult, null);
		} else {
			// playBeepSoundAndVibrate();// 播放声音和振动代表成功获取二维码
			drawResultPoints(barcode, rawResult);

			switch (source) {
			case NATIVE_APP_INTENT:
			case PRODUCT_SEARCH_LINK:
				handleDecodeExternally(rawResult, barcode);
				break;
			case ZXING_LINK:
				if (returnUrlTemplate == null) {
					handleDecodeInternally(rawResult, barcode);
				} else {
					handleDecodeExternally(rawResult, barcode);
				}
				break;
			case NONE: {
				handleDecodeInternally(rawResult, barcode);
			}
				break;
			}
		}
	}

	/**
	 * 把图片截图下来之后,标记二维码所在的点 Superimpose a line for 1D or dots for 2D to highlight
	 * the key features of the barcode.
	 * 
	 * @param barcode
	 *            A bitmap of the captured image.
	 * @param rawResult
	 *            The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_image_border));
			paint.setStrokeWidth(3.0f);
			paint.setStyle(Paint.Style.STROKE);
			Rect border = new Rect(2, 2, barcode.getWidth() - 2,
					barcode.getHeight() - 2);
			canvas.drawRect(border, paint);
			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat()
							.equals(BarcodeFormat.UPC_A))
					|| (rawResult.getBarcodeFormat()
							.equals(BarcodeFormat.EAN_13))) {
				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	private void handleDecodeExternally(Result rawResult, Bitmap barcode) {
		viewfinderView.drawResultBitmap(barcode);
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(VIBRATE_DURATION);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			NormalUtil.displayFrameworkBugMessageAndExit(Search.this);
			return;
		} catch (RuntimeException e) {
			NormalUtil.displayFrameworkBugMessageAndExit(Search.this);
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void setTopBar() {
		TopBar topBar = (TopBar) findViewById(R.id.topBar);
		topBar.hiddenLeftButton(true);
		topBar.hiddenRightButton(false);
		topBar.setRightDrawable(R.drawable.close);
		topBar.setTopBarClickListener(new TopBarClickListener() {

			@Override
			public void rightBtnClick() {

				finish();

			}

			@Override
			public void leftBtnClick() {

			}
		});
		topBar.setTitle(getResources().getString(R.string.title));
	}

	private void resetPreview() {
		if (handler != null) {
			handler.sendEmptyMessage(R.id.restart_preview);
		}
	}

	public final void showWaitDialog(String str) {
		Message message = handler2.obtainMessage();
		message.what = 1;
		message.obj = str;
		handler2.sendMessage(message);
	}

	public final void closeWaitDialog() {
		Message message = handler2.obtainMessage();
		message.what = 0;
		handler2.sendMessage(message);
	}

	private ProgressDialog waitDialog;

	private final void doShowWaitDialog(String str) {
		if (waitDialog == null)
			waitDialog = new ProgressDialog(this);
		waitDialog.setMessage(str);
		waitDialog.show();
	}

	private final void doCloseWaitDialog() {
		if (waitDialog != null) {
			waitDialog.cancel();
			waitDialog = null;
		}
	}

	private String decode(String souceCode) {
		if (souceCode == null || souceCode.equals(""))
			return null;
		if (souceCode.length() < 4 || !souceCode.contains("*"))
			return null;
		String[] oo = souceCode.split("\\*");
		byte offset = 0;
		byte[] bytes = new byte[oo.length - 1];
		String result;
		try {
			offset = Byte.valueOf(oo[oo.length - 1]);
			for (int j = 0; j < oo.length - 1; j++) {
				bytes[j] = (byte) (Byte.parseByte(oo[j]) - offset);
			}
			result = new String(bytes, "utf-8");
			System.out.println(result);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}

	private void saveBitmap(Bitmap bitmap) {
		Date date = new java.util.Date();
		String dateTime = new SimpleDateFormat("MM-dd-HH-mm-ss").format(date);
		String imgName = dateTime + ".jpg";
		String imgPath = NormalUtil.getRootDir() + imgName;
		bitmap = ImageUtil.setBitmapRotate(90, bitmap);
		ImageUtil.saveBitmapToSDcard(imgPath, bitmap, 100);
		MessageItem item = new MessageItem();
		item.setPhotoPath(imgPath);
		item.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		item.setId(date.getTime());
		DBTool.getInstance().saveMessage(getApplicationContext(), item);
	}

}