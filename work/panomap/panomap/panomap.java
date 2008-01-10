import org.w3c.dom.*;
import org.apache.xerces.parsers.DOMParser;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.font.*;
import java.text.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Math;
import java.applet.*;
import java.net.*;

public class panomap extends Applet implements ActionListener,ItemListener {
	static int FRAMEW = 760;			// フレーム幅
	static int FRAMEH = 640;			// フレーム高さ
	static int SPACEX = 0;				// 左のスペースＸ
	static int SPACEY = 0;				// 上のスペースＹ
	static int CANVASX = 0;				// キャンバスＸ
	static int CANVASY = 25;			// キャンバスＹ
	static int CANVASW = 760;			// キャンバス幅
	static int CANVASH = 580;			// キャンバス高さ
	static int BTNY = 25;				// 上のボタンＹ

	static int pisize = 240;			// パノラマ表示基準サイズ
	static int misize = 240;			// 地図表示基準サイズ
	static int colayout = 0;			// ボタンのレイアウト（0:上、1:下、2:左、3:右）
	static int disptype = 0;			// 表示種別（0:標準、1:ＨＰ、2:地図、3:道案内）
	static String scenariofile = "";	// シナリオファイル
	static int scenariono = -1;			// シナリオ番号

	static int PANOY1 = 20;				// パノラマ表示基準位置ｙ
	static int PANOW = 480;				// パノラマ表示幅
	static int PANOH = 240;				// パノラマ表示高さ
	static int ARWW1 = 30;				// 矢印１表示幅
	static int ARWW2 = 40;				// 矢印２表示幅
	static int ARWH = 30;				// 矢印高さ
	static int MAPY1 = 320;				// 地図表示基準位置ｙ
	static int MAPW = 240;				// 地図表示幅
	static int MAPH = 240;				// 地図表示高さ
	static int FUKIW = 160;				// ふきだし表示幅
	static int FUKIH = 240;				// ふきだし表示高さ
	static int FUKIY1 = 320;			// ふきだし表示ｙ

	static int MMX = CANVASW/2;			// 地図の中心ｘ
	static int MMY = MAPY1+MAPH/2;		// 地図の中心ｙ
	static int MX1 = MMX-MAPW/2;		// 地図の左位置
	static int MX2 = MMX+MAPW/2;		// 地図の右位置
	static int PMX = CANVASW/2;			// パノラマの中心ｘ
	static int PMY = PANOY1+PANOH/2;	// パノラマの中心ｙ
	static int PX1 = PMX-PANOW/2;		// パノラマの左位置
	static int PX2 = PMX+PANOW/2;		// パノラマの右位置

	static int MOIS = 28;				// ＰＯＩ表示サイズ（アイコン）
	static int MPPS = 8;				// 地図パノラマポイント表示サイズ（○）
	int mois = MOIS;					// ＰＯＩ表示サイズ（アイコン）
	int mpps = MPPS;					// 地図パノラマポイント表示サイズ（○）

	static int MIS = 10;				// 隙間
	static int ICONSIZE = 16;			// マップアイコンサイズ

	static int PANO_ROTA_UNIT = 8;		// パノラマ回転単位
	static int PANO_ZOOM_UNIT = 10;		// パノラマズーム単位
	static int PANO_ZOOM_MAX = 300;		// パノラマズーム最大
	static int PANO_ZOOM_MIN = 30;		// パノラマズーム最小
	static int MAP_MOVE_UNIT = 20;		// 地図移動単位
	static int MAP_ZOOM_UNIT = 10;		// 地図ズーム単位
	static int MAP_ZOOM_MAX = 300;		// 地図ズーム最大
	static int MAP_ZOOM_MIN = 20;		// 地図ズーム最小
	static int MAP_ROTA_UNIT = 30;		// 地図回転単位
	static int LINKIDMIN = 1000;		// リンクＩＤ開始値
	static int WARPIDMIN = 2000;		// ワープＩＤ開始値
	static int CONDS_MAX = 4;			// 条件最大数
	static int COND_MAX = 8;			// 条件の項目最大数
	static String INITDATA_XML = "initdata.xml";// 初期データ
	static String SSBTNLABEL1 = "シナリオ開始";// シナリオ開始ボタンラベル
	static String SSBTNLABEL2 = "再開";// シナリオ開始ボタンラベル
	static String SSBTNLABEL3 = "一時停止";// シナリオ開始ボタンラベル
	static String SCBTNLABEL1 = "コマ送り";// コマ送りボタンラベル
	static String SCBTNLABEL2 = "コマ送り";// コマ送りボタンラベル
	static String SCBTNLABEL3 = "中止";// コマ送りボタンラベル
	static int DEF_DELAY2 = 200;		// タイマー２遅延(ms)
	static int DEF_PERIOD2 = 50;		// タイマー２間隔(ms)
	static String[] BTNMSG = {"","パノラマ：３０度ずつ左を向きます。","パノラマ：少しずつ左を向きます。",
							"パノラマ：少しずつ右を向きます。","パノラマ：３０度ずつ右を向きます。",
							"パノラマ：縮小表示します。","パノラマ：拡大表示します。",
							"地図：左へ移動します。","地図：右へ移動します。",
							"地図：上へ移動します。","地図：下へ移動します。",
							"地図：縮小表示します。","地図：拡大表示します。",
							"地図：左に３０度ずつ回転します。","地図：右に３０度ずつ回転します。"};

	Canvas canvas1 = new TestCanvas();
	Canvas cvLogo;
	Panel pnMain = new Panel();
	Panel pnPano = new Panel();
	Panel pnMap1 = new Panel();
	Panel pnDum1 = new Panel();
	TextField tfMapxml = new TextField();
	Choice choiceMapxml = new Choice();
	Choice[] choiceCond = new Choice[CONDS_MAX];
	TextField tfScena = new TextField();
	Choice choScena = new Choice();
	Choice choSpeed = new Choice();
	TextArea textArea1 = new TextArea("",0,0,TextArea.SCROLLBARS_NONE);
	TextArea textArea2 = new TextArea("",0,0,TextArea.SCROLLBARS_NONE);
	Checkbox checkbox1 = new Checkbox();
	Checkbox checkbox2 = new Checkbox();
	Checkbox checkbox3 = new Checkbox();
	Checkbox checkbox4 = new Checkbox();
	Checkbox checkbox5 = new Checkbox();
	Checkbox checkbox6 = new Checkbox();
	Checkbox checkbox7 = new Checkbox();
	Checkbox chkWaku = new Checkbox();
	Button btn1 = new Button("読み込み");
	Button btn2 = new Button("初期位置");
	Button SRBtn = new Button("シナリオ読込");
	Button SSBtn = new Button(SSBTNLABEL1);
	Button SCBtn = new Button(SCBTNLABEL1);
	Label label1 = new Label();
	Label label2 = new Label();

	MediaTracker mt;
	int mtid = 0;
	String mapxmlfilename = "";			// 地図データＸＭＬ
	String map_filename = "";			// 地図画像ファイル
	static String initidparam = "";		// 初期地点(パラメタ)
	static String initmukiparam = "";	// 初期向き(パラメタ)
	String initidstring = "";			// 初期地点(String)
	String initmukistring = "";			// 初期向き(String)
	Image img_map;						// 地図
	Image img_arwicon;					// 矢印アイコン
	Image img_mapicon;					// 地図操作アイコン
	Image img_guide;					// ガイド
	Image img_logo;						// ロゴ
	int mapw = 0;						// 地図幅
	int maph = 0;						// 地図高さ
	int mapx = 120;						// 地図表示位置
	int mapy = 120;						// 地図表示位置
	int mapa = 0;						// 地図表示角度
	int maprate = 100;					// 地図表示倍率（％）
	int initpoint = 0;					// 初期地点
	int point = 0;						// 現在地点
	int backpoint = 0;					// 直前地点
	int initmuki = 120;					// 初期向き
	int muki = 120;						// 現在向き
	int backmuki = 120;					// 直前向き

	Timer tm1;							// タイマー１（移動用）
	Timer tm2;							// タイマー２（パノラマ、地図操作用）
	TimerTask1 task1 = null;			// タイマー１タスク
	TimerTask2 task2 = null;			// タイマー２タスク
	long period1 = 500;					// タイマー１間隔(ms)
	long period2 = 50;					// タイマー２間隔(ms)
	LinkDataSet[] links;
	LinkSet[] link;
	Vector v,PanoId,PanoUri,PanoMx,PanoMy,PanoAngle,PanoSkip,PataUri,LinkUri,LinkId,WarpId;
	Vector FpoiMx,FpoiMy,FpoiIcon,FpoiImg,FpoiTxt,NpoiMx,NpoiMy,NpoiIcon;
	Vector Scene,Mapxml;
	Vector[] Cond = new Vector[CONDS_MAX];
	String[] Condkey = new String[CONDS_MAX];
	Panorama[] Pano;
	MapObj[] Fpoi;						// ふきだしＰＯＩ
	MapObj[] Npoi;						// 名前ＰＯＩ
	Image[] PataImg;
	String[] LinkObj;
	StringBuffer scene = new StringBuffer("");
	String scenemsg1 = "";				// シナリオ回転中メッセージ
	String scenemsg2 = "";				// シナリオ直進中メッセージ
	int fpoi_no = -1;					// ふきだしＰＯＩ番号
	int fpoi_mode = 0;					// ふきだし固定モード
	int pata_mode = 0;					// パタパタモード(1:パタパタ準備,2:パタパタ,3:回転,4:一時停止準備,5:一時停止,6:終了準備)
	int pata_from = 0;					// パタパタ開始番号
	int pata_now = 0;					// パタパタ現在番号
	int pata_to = 0;					// パタパタ終了番号
	int pata_index = 0;					// パタパタ領域インデックス
	int pata_muki = 0;					// パタパタ目標向き
	int pata_speed = 1;					// パタパタスピード
	int rota_mode = 0;					// 回転モード
	int d_muki = 0;						// 回転の増分
	int waku_mode = 1;					// 枠表示モード
	int waku_no = -1;					// 枠表示番号
	int button_no = -1;					// ボタン説明表示番号
	int map_mode = 0;					// 地図操作モード
	int d_mapx = 0;						// 移動の増分
	int d_mapy = 0;						// 移動の増分
	int d_maprate = 0;					// 拡大縮小の増分
	int d_mapa = 0;						// 回転の増分
	int pano_w = 0;						// パノラマ幅
	int pano_h = 0;						// パノラマ高さ
	int pano_a = 0;						// パノラマ開始角度
	int panorate = 100;					// パノラマ表示倍率（％）
	int d_panorate = 0;					// 拡大縮小の増分
	int map_imgmode = 1;				// 写真表示モード
	int map_centermode = 0;				// 現在位置中心固定モード
	int map_upmode = 0;					// 現在向き上固定モード
	int map_popmode = 0;				// ふきだしモード
	int map_scemode = 0;				// シナリオモード
	int map_dispamode = 1;				// 方角表示モード
	int map_disprmode = 0;				// 全ルート表示モード
	int cursor = 0;						// カーソル形状
	int norepaint = 0;					// 再描画しないフラグ
	int dragsx = 0;						// ドラッグ開始ｘ
	Color bcolor = Color.black;			// 背景カラー
	Color fcolor = Color.white;			// 文字カラー
	Color wcolor = Color.darkGray;		// ワクカラー
	Color hcolor = new Color(255,255,225);	// ふきだしカラー
	Color pbcolor = Color.lightGray;	// パネル背景カラー
	Color pfcolor = Color.black;		// パネル文字カラー
	TextPlayer textPlayer;
	PicturePlayer picturePlayer;
	final static BasicStroke stroke1 = new BasicStroke(1.0f);
	final static BasicStroke stroke2 = new BasicStroke(2.0f);

	class TestCanvas extends Canvas {
		BufferedImage back_image = null;	// バッファ
		Graphics2D back_g;					// バッファのGraphicsオブジェクト

		public void paint(Graphics g) {

		if (norepaint == 1) return;

		Graphics2D g2 = (Graphics2D) g;

		if (back_image == null) {
			back_image = (BufferedImage)createImage(getSize().width,getSize().height);
			back_g = (Graphics2D)back_image.createGraphics();
		}

		if (Pano[point].img == null) {// 未ロードならロードする
			Pano[point].img = LoadImage(PanoUri.elementAt(point).toString());
		}

		pano_w = Pano[point].img.getWidth(this);
		pano_h = Pano[point].img.getHeight(this);

//		if (rota_mode == 0) {// パノラマ回転中以外
			back_g.setColor(bcolor);// 背景カラー
			back_g.fillRect(0, PANOY1+PANOH+ICONSIZE, CANVASW, BTNY+MIS*4+MAPH);// 塗りつぶす
			paintMap(g2);// 地図を描画
//		}
		if (disptype != 2) {
			back_g.setColor(bcolor);// 背景カラー
			back_g.fillRect(0, PANOY1-ICONSIZE, CANVASW, PANOH+ICONSIZE*2);// 塗りつぶす
			paintPano(g2);// パノラマを描画
		}

		g2.drawImage(back_image,0,0,this);

		}// end of paint()

		public void paintMap(Graphics2D g2) {

		// 地図を描画
		back_g.setClip(MX1, MAPY1, MAPW, MAPH);// クリップ領域を指定
		if (map_centermode == 1) {// 現在位置中心固定モード
			if (pata_mode != 2) {// パノラマ
				mapx = Pano[point].mx;
				mapy = Pano[point].my;
			} else {// パタパタ
				mapx = Pano[backpoint].mx + (Pano[point].mx-Pano[backpoint].mx)*(pata_now-pata_from+1)/(pata_to-pata_from+2);
				mapy = Pano[backpoint].my + (Pano[point].my-Pano[backpoint].my)*(pata_now-pata_from+1)/(pata_to-pata_from+2);
			}
		}
		if (map_upmode == 1) {// 現在向き上固定モード
			mapa = (Pano[point].angle - muki*360/pano_w + 270 + 360) % 360;// 角度を調整
		}
		int drx = MAPW/2;// 表示サイズの半分
		int dry = MAPH/2;// 表示サイズの半分
		int mrx = drx*100/maprate;// 表示サイズの半分に表示可能な地図サイズ
		int mry = dry*100/maprate;// 表示サイズの半分に表示可能な地図サイズ
		BufferedImage map_image = (BufferedImage)createImage(MAPW*3/2, MAPH*3/2);// まず１．５倍で描き、真ん中だけ使う
		Graphics2D map_g = (Graphics2D)map_image.createGraphics();
		map_g.rotate(mapa*Math.PI/180.0, MAPW*3/4, MAPH*3/4);// 中心で回転
		int msx = Math.max(0, mrx*3/2-mapx);// 地図非表示部分（黒い部分）の地図サイズ
		int msy = Math.max(0, mry*3/2-mapy);// 地図非表示部分（黒い部分）の地図サイズ
		int dsx = msx*maprate/100;// 地図非表示部分（黒い部分）の表示サイズ
		int dsy = msy*maprate/100;// 地図非表示部分（黒い部分）の表示サイズ
		map_g.drawImage(img_map, dsx, dsy, MAPW*3/2, MAPH*3/2, mapx-mrx*3/2+msx, mapy-mry*3/2+msy, mapx+mrx*3/2, mapy+mry*3/2, this);// 地図画像表示
		back_g.drawImage(map_image, MX1, MAPY1, MX2, MAPY1+MAPH, MAPW/4, MAPH/4, MAPW*5/4, MAPH*5/4, this);// 地図画像表示
//		back_g.drawImage(img_map, MX1, MAPY1, MX2, MAPY1+MAPH, mapx-mrx, mapy-mry, mapx+mrx, mapy+mry, this);// 地図画像表示
		map_g.dispose();
		map_image.flush();
		// 建物ポイント、建物画像を描画
		int ddx, ddy;
		int robj_id = -1;
		int lobj_id = -1;
		int robj_dist = drx*drx + dry*dry;
		int lobj_dist = drx*drx + dry*dry;
		int robj_ddx = 0;
		int robj_ddy = 0;
		int lobj_ddx = 0;
		int lobj_ddy = 0;
		for (int i=0; i<Fpoi.length; i++) {
			ddx = Calc_ddx(Fpoi[i].mx, Fpoi[i].my);// 表示中心からの相対位置
			ddy = Calc_ddy(Fpoi[i].mx, Fpoi[i].my);// 表示中心からの相対位置
			if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// 表示範囲内
				if (map_imgmode == 0) {// 写真非表示
					back_g.setColor(Color.blue);// 青
					back_g.fillOval(MMX+ddx-mpps, MMY+ddy-mpps, mpps*2, mpps*2);// 地図上に建物ポイント表示
					back_g.setColor(Color.black);// 黒
					back_g.drawOval(MMX+ddx-mpps, MMY+ddy-mpps, mpps*2, mpps*2);// 地図上に建物ポイント表示
				} else {// 写真表示
					if (Fpoi[i].icon == null) {// 未ロードならロードする
						Fpoi[i].icon = LoadImage(FpoiIcon.elementAt(i).toString());
					}
					back_g.drawImage(Fpoi[i].icon, MMX+ddx-mois, MMY+ddy-mois, mois*2, mois*2, this);// 地図上に建物画像表示
				}
			} else {// 表示範囲外
				continue;
			}
			if (map_popmode == 1) {// ふきだしモード
				if (ddx >= 0 && ddy < 0) {// 右上範囲内
					int dist = ddx*ddx + ddy*ddy;// 距離の２乗
					if (dist < robj_dist) {
						robj_id = i;
						robj_dist = dist;
						robj_ddx = ddx;
						robj_ddy = ddy;
					}
				} else if (ddx < 0 && ddy < 0) {// 左上範囲内
					int dist = ddx*ddx + ddy*ddy;// 距離の２乗
					if (dist < lobj_dist) {
						lobj_id = i;
						lobj_dist = dist;
						lobj_ddx = ddx;
						lobj_ddy = ddy;
					}
				}
			}
			if (map_popmode == 0 && fpoi_no == i) {
				if (disptype == 2 || disptype == 3) {// 地図レイアウトまたは道案内レイアウト
					lobj_id = i;
					lobj_ddx = ddx;
					lobj_ddy = ddy;
				} else {
					if (ddx >= 0) {// 右範囲内
						robj_id = i;
						robj_ddx = ddx;
						robj_ddy = ddy;
					} else {// 左範囲内
						lobj_id = i;
						lobj_ddx = ddx;
						lobj_ddy = ddy;
					}
				}
			}
		}
		// 名前ＰＯＩを描画
		for (int i=0; i<Npoi.length; i++) {
			ddx = Calc_ddx(Npoi[i].mx, Npoi[i].my);// 表示中心からの相対位置
			ddy = Calc_ddy(Npoi[i].mx, Npoi[i].my);// 表示中心からの相対位置
			if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// 表示範囲内
				if (Npoi[i].icon == null) {// 未ロードならロードする
					Npoi[i].icon = LoadImage(NpoiIcon.elementAt(i).toString());
				}
				back_g.drawImage(Npoi[i].icon, MMX+ddx-Npoi[i].icon.getWidth(this)/2, MMY+ddy-Npoi[i].icon.getHeight(this)/2, this);// 地図上に名前ＰＯＩ表示
			}
		}
		// パノラマポイントを描画
		back_g.setColor(Color.black);// 黒
		for (int i=0; i<Pano.length; i++) {
			ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
			ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
			if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// 表示範囲内
				back_g.drawOval(MMX+ddx-mpps, MMY+ddy-mpps, mpps*2, mpps*2);// 地図上にパノラマポイント表示
			}
		}
		// 全ルートを描画
		if (map_disprmode == 1) {// 全ルート表示モード
			int ddx2, ddy2;
			back_g.setColor(Color.black);// 黒
			for (int i=0; i<Pano.length; i++) {
				ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
				ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
				for (int j=0; j<links[i].linkSet.area.length; j++){// 枠表示ループ
					LinkArea la = links[i].linkSet.area[j];
					if (la.id >= 0 && la.id <= LINKIDMIN-1) {// パタパタへのリンク
						ddx2 = Calc_ddx(Pano[la.id].mx, Pano[la.id].my);// 表示中心からの相対位置
						ddy2 = Calc_ddy(Pano[la.id].mx, Pano[la.id].my);// 表示中心からの相対位置
						if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry ||
							-drx < ddx2 && ddx2 < drx && -dry < ddy2 && ddy2 < dry) {// どちらかが表示範囲内
							back_g.drawLine(MMX+ddx, MMY+ddy, MMX+ddx2, MMY+ddy2);// 地図上にルート表示
						}
//						if (-drx < ddx2 && ddx2 < drx && -dry < ddy2 && ddy2 < dry) {// 先が表示範囲内
//							int ar = 8;// サイズ
//							double theta = Math.atan2((double)(ddy-ddy2), (double)(ddx-ddx2));
//							int dax1 = ddx2+(int)(ar*Math.cos(theta-Math.PI/8.0));
//							int day1 = ddy2+(int)(ar*Math.sin(theta-Math.PI/8.0));
//							back_g.drawLine(MMX+ddx2, MMY+ddy2, MMX+dax1, MMY+day1);
//							int dax2 = ddx2+(int)(ar*Math.cos(theta+Math.PI/8.0));
//							int day2 = ddy2+(int)(ar*Math.sin(theta+Math.PI/8.0));
//							back_g.drawLine(MMX+ddx2, MMY+ddy2, MMX+dax2, MMY+day2);
//						}
					}
				}
			}
		}
		// シナリオルートを描画
		if (map_scemode == 1) {// シナリオモード
			back_g.setStroke(stroke2);
			int ddx0 = 9999;
			int ddy0 = 9999;
			String sce = (String)Scene.elementAt(choScena.getSelectedIndex());
			StringTokenizer st = new StringTokenizer(sce, ";");
			back_g.setColor(Color.blue);// 青
			while (st.hasMoreTokens()) {
				String nextstr = st.nextToken();
				nextstr = nextstr.substring(0,nextstr.indexOf(','));
				if (nextstr.equals("stop")) continue;
//				int spoint = Integer.parseInt(nextstr);
				int spoint = getId(nextstr);// ＩＤ番号を取得
				if (spoint < 0) break;
				if (spoint >= LINKIDMIN) continue;
				ddx = Calc_ddx(Pano[spoint].mx, Pano[spoint].my);// 表示中心からの相対位置
				ddy = Calc_ddy(Pano[spoint].mx, Pano[spoint].my);// 表示中心からの相対位置
				if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// 表示範囲内
					back_g.drawOval(MMX+ddx-mpps, MMY+ddy-mpps, mpps*2, mpps*2);// 地図上にシナリオポイント表示
				}
				if (ddx0 != 9999 && ddy0 != 9999) {// 最初以外
					back_g.drawLine(MMX+ddx, MMY+ddy, MMX+ddx0, MMY+ddy0);// 地図上にシナリオルート表示
				}
				ddx0 = ddx;
				ddy0 = ddy;
			}
			back_g.setStroke(stroke1);
		}
		// 移動体を描画
		if (disptype != 2) {// 地図レイアウト以外
			int mx, my;
			if (pata_mode != 2) {// パノラマ
				mx = Pano[point].mx;
				my = Pano[point].my;
			} else {// パタパタ
				mx = Pano[backpoint].mx + (Pano[point].mx-Pano[backpoint].mx)*(pata_now-pata_from+1)/(pata_to-pata_from+2);
				my = Pano[backpoint].my + (Pano[point].my-Pano[backpoint].my)*(pata_now-pata_from+1)/(pata_to-pata_from+2);
			}
			ddx = Calc_ddx(mx, my);// 表示中心からの相対位置
			ddy = Calc_ddy(mx, my);// 表示中心からの相対位置
			if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// 表示範囲内
				int pr = 5;// 移動体サイズ
				int angle = mapa - Pano[point].angle + muki*360/pano_w;
				int dax1 = (int)(Math.cos(angle*Math.PI/180.0)*pr*3);
				int day1 = (int)(Math.sin(angle*Math.PI/180.0)*pr*3);
				int dax2 = (int)(Math.cos((angle+90)*Math.PI/180.0)*pr);
				int day2 = (int)(Math.sin((angle+90)*Math.PI/180.0)*pr);
				int dax3 = (int)(Math.cos((angle-90)*Math.PI/180.0)*pr);
				int day3 = (int)(Math.sin((angle-90)*Math.PI/180.0)*pr);
				int polyx[] = {MMX+ddx+dax1, MMX+ddx+dax2, MMX+ddx+dax3};
				int polyy[] = {MMY+ddy+day1, MMY+ddy+day2, MMY+ddy+day3};
				back_g.setColor(Color.red);// 赤
				back_g.fillOval(MMX+ddx-pr, MMY+ddy-pr, pr*2, pr*2);// 現在ポイント表示（円）
				back_g.fillPolygon(polyx, polyy, 3);// 現在ポイント表示（三角形）
			}
		}
		// 方角表示を描画
		if (map_dispamode == 1) {// 方角表示モード
			int ar = 5;// サイズ
			int dax1 = (int)(Math.cos((mapa-90)*Math.PI/180.0)*ar*3);
			int day1 = (int)(Math.sin((mapa-90)*Math.PI/180.0)*ar*3);
			int dax2 = (int)(Math.cos(mapa*Math.PI/180.0)*ar);
			int day2 = (int)(Math.sin(mapa*Math.PI/180.0)*ar);
			int dax3 = (int)(Math.cos((mapa+90)*Math.PI/180.0)*ar*3);
			int day3 = (int)(Math.sin((mapa+90)*Math.PI/180.0)*ar*3);
			int dax4 = (int)(Math.cos((mapa+180)*Math.PI/180.0)*ar);
			int day4 = (int)(Math.sin((mapa+180)*Math.PI/180.0)*ar);
			int polyx1[] = {MX2-ar*3+dax1, MX2-ar*3+dax2, MX2-ar*3+dax4};
			int polyy1[] = {MAPY1+ar*3+day1, MAPY1+ar*3+day2, MAPY1+ar*3+day4};
			int polyx2[] = {MX2-ar*3+dax3, MX2-ar*3+dax4, MX2-ar*3+dax2};
			int polyy2[] = {MAPY1+ar*3+day3, MAPY1+ar*3+day4, MAPY1+ar*3+day2};
			int polyx3[] = {MX2-ar*3+dax1, MX2-ar*3+dax2, MX2-ar*3+dax3, MX2-ar*3+dax4};
			int polyy3[] = {MAPY1+ar*3+day1, MAPY1+ar*3+day2, MAPY1+ar*3+day3, MAPY1+ar*3+day4};
			back_g.setColor(Color.lightGray);// グレー
			back_g.fillOval(MX2-ar*6, MAPY1, ar*6, ar*6);// 方角表示（円）
			back_g.setColor(Color.red);// 赤
			back_g.fillPolygon(polyx1, polyy1, 3);// 方角表示（三角形）
			back_g.setColor(Color.white);// 白
			back_g.fillPolygon(polyx2, polyy2, 3);// 方角表示（三角形）
			back_g.setColor(Color.black);// 黒
			back_g.drawOval(MX2-ar*6, MAPY1, ar*6, ar*6);// 方角表示（円）
			back_g.drawPolygon(polyx3, polyy3, 4);// 方角表示（四角形）
		}
		back_g.setClip(null);// クリップ領域を解除
		// 地図まわりの矢印を描画
		back_g.setColor(wcolor);// ワクカラー
		if (disptype != 2) {// 地図レイアウト以外
			back_g.fill3DRect(MX1-ICONSIZE, MAPY1+MAPH, ICONSIZE, ICONSIZE, true); // 左下四角
			back_g.drawImage(img_mapicon, MX1-ICONSIZE, MAPY1+MAPH, MX1, MAPY1+MAPH+ICONSIZE, 0, 32, 16, 48, this);// 縮小画像表示
			back_g.fill3DRect(MX2, MAPY1+MAPH, ICONSIZE, ICONSIZE, true); // 右下四角
			back_g.drawImage(img_mapicon, MX2, MAPY1+MAPH, MX2+ICONSIZE, MAPY1+MAPH+ICONSIZE, 32, 32, 48, 48, this);// 拡大画像表示
		} else {
			back_g.fill3DRect(MX1-MIS, MAPY1+MAPH, MIS, MIS, true); // 左下四角
			back_g.fill3DRect(MX2, MAPY1+MAPH, MIS, MIS, true); // 右下四角
		}
		if (disptype != 2 && map_upmode == 0) {// 地図レイアウト以外かつ上固定でない
			back_g.fill3DRect(MX1-ICONSIZE, MAPY1-ICONSIZE, ICONSIZE, ICONSIZE, true); // 左上四角
			back_g.drawImage(img_mapicon, MX1-ICONSIZE, MAPY1-ICONSIZE, MX1, MAPY1, 0, 0, 16, 16, this);// 左回転画像表示
			back_g.fill3DRect(MX2, MAPY1-ICONSIZE, ICONSIZE, ICONSIZE, true); // 右上四角
			back_g.drawImage(img_mapicon, MX2, MAPY1-ICONSIZE, MX2+ICONSIZE, MAPY1, 32, 0, 48, 16, this);// 右回転画像表示
		} else {
			back_g.fill3DRect(MX1-MIS, MAPY1-MIS, MIS, MIS, true); // 左上四角
			back_g.fill3DRect(MX2, MAPY1-MIS, MIS, MIS, true); // 右上四角
		}
		back_g.setClip(MX1-1, MAPY1-MIS, MAPW+2, MIS);// クリップ領域を指定
		back_g.fill3DRect(MX1-MIS, MAPY1-MIS, MAPW+MIS*2, MIS, true); // 地図上四角
		back_g.setClip(MX1-1, MAPY1+MAPH, MAPW+2, MIS);// クリップ領域を指定
		back_g.fill3DRect(MX1-MIS, MAPY1+MAPH, MAPW+MIS*2, MIS, true); // 地図下四角
		back_g.setClip(MX1-MIS, MAPY1-1, MIS, MAPH+2);// クリップ領域を指定
		back_g.fill3DRect(MX1-MIS, MAPY1-2, MIS, MAPH+4, true); // 地図左四角
		back_g.setClip(MX2, MAPY1-1, MIS, MAPH+2);// クリップ領域を指定
		back_g.fill3DRect(MX2, MAPY1-2, MIS, MAPH+4, true); // 地図右四角
		if (disptype != 2 && map_centermode == 0) {// 地図レイアウト以外かつ中心固定でない
			back_g.setClip(MX1-ICONSIZE, MMY-ICONSIZE/2, ICONSIZE-MIS+1, ICONSIZE);// クリップ領域を指定
			back_g.fill3DRect(MX1-ICONSIZE, MMY-ICONSIZE/2, ICONSIZE, ICONSIZE, true); // 地図左四角
			back_g.setClip(MX2+MIS-1, MMY-ICONSIZE/2, ICONSIZE-MIS+1, ICONSIZE);// クリップ領域を指定
			back_g.fill3DRect(MX2, MMY-ICONSIZE/2, ICONSIZE, ICONSIZE, true); // 地図左四角
			back_g.setClip(MMX-ICONSIZE/2, MAPY1-ICONSIZE, ICONSIZE, ICONSIZE-MIS+1);// クリップ領域を指定
			back_g.fill3DRect(MMX-ICONSIZE/2, MAPY1-ICONSIZE, ICONSIZE, ICONSIZE, true); // 地図左四角
			back_g.setClip(MMX-ICONSIZE/2, MAPY1+MAPH+MIS-1, ICONSIZE, ICONSIZE-MIS+1);// クリップ領域を指定
			back_g.fill3DRect(MMX-ICONSIZE/2, MAPY1+MAPH, ICONSIZE, ICONSIZE, true); // 地図左四角
			back_g.setClip(null);// クリップ領域を解除
			back_g.drawImage(img_mapicon, MX1-ICONSIZE, MMY-ICONSIZE/2, MX1, MMY+ICONSIZE/2, 0, 16, 16, 32, this);// 左矢印画像表示
			back_g.drawImage(img_mapicon, MX2, MMY-ICONSIZE/2, MX2+ICONSIZE, MMY+ICONSIZE/2, 32, 16, 48, 32, this);// 右矢印画像表示
			back_g.drawImage(img_mapicon, MMX-ICONSIZE/2, MAPY1-ICONSIZE, MMX+ICONSIZE/2, MAPY1, 16, 0, 32, 16, this);// 上矢印画像表示
			back_g.drawImage(img_mapicon, MMX-ICONSIZE/2, MAPY1+MAPH, MMX+ICONSIZE/2, MAPY1+MAPH+ICONSIZE, 16, 32, 32, 48, this);// 下矢印画像表示
		} else {
			back_g.setClip(null);// クリップ領域を解除
		}
		// テキストエリア非表示
		if (map_popmode == 0 && fpoi_no < 0) {
			if (textArea1.isVisible()) textArea1.setVisible(false);
			if (textArea2.isVisible()) textArea2.setVisible(false);
		}
		// ふきだしを描画
		if (robj_id >= 0) {// 右にオブジェクトがある？
			int poly_x[] = {MMX+robj_ddx, MX2+MIS*2+1, MX2+MIS*2+1};
			int poly_y[] = {MMY+robj_ddy, FUKIY1+FUKIW/2+MIS, FUKIY1+FUKIW/2-MIS};
			back_g.setColor(hcolor);// ふきだしカラー
			back_g.fillRoundRect(MX2+MIS*2, FUKIY1, FUKIW, FUKIH, MIS*2, MIS*2);// フキダシ（角丸四角）表示
			back_g.setColor(Color.black);// 黒
			back_g.drawRoundRect(MX2+MIS*2, FUKIY1, FUKIW, FUKIH, MIS*2, MIS*2);// フキダシ（角丸四角）表示
			back_g.setColor(hcolor);// ふきだしカラー
			back_g.fillPolygon(poly_x, poly_y, 3);// フキダシ（三角）表示
			back_g.setColor(Color.black);// 黒
			back_g.drawLine(poly_x[0], poly_y[0], poly_x[1], poly_y[1]);// フキダシ（線）表示
			back_g.drawLine(poly_x[0], poly_y[0], poly_x[2], poly_y[2]);// フキダシ（線）表示
			if (FpoiImg.elementAt(robj_id).toString().length() > 0) {
				if (Fpoi[robj_id].img == null) {// 未ロードならロードする
					Fpoi[robj_id].img = LoadImage(FpoiImg.elementAt(robj_id).toString());
				}
				back_g.drawImage(Fpoi[robj_id].img, MX2+MIS*3, FUKIY1+MIS, FUKIW-MIS*2, FUKIW-MIS*2, this);// 建物画像表示
			}
			if (FpoiTxt.elementAt(robj_id).toString().length() > 0) {
				if (Fpoi[robj_id].txt == null) {// 未ロードならロードする
					Fpoi[robj_id].txt = LoadText(FpoiTxt.elementAt(robj_id).toString());
				}
				if (!textArea2.isVisible()) textArea2.setVisible(true);
				textArea2.setText(Fpoi[robj_id].txt);// 建物説明表示
			}
		} else {
			if (textArea2.isVisible()) textArea2.setVisible(false);
		}
		if (lobj_id >= 0) {// 左にオブジェクトがある？
			int poly_x[] = {MMX+lobj_ddx, MX1-MIS*2, MX1-MIS*2};
			int poly_y[] = {MMY+lobj_ddy, FUKIY1+FUKIW/2+MIS, FUKIY1+FUKIW/2-MIS};
			back_g.setColor(hcolor);// ふきだしカラー
			back_g.fillRoundRect(MX1-MIS*2-FUKIW, FUKIY1, FUKIW, FUKIH, MIS*2, MIS*2);// フキダシ（角丸四角）表示
			back_g.setColor(Color.black);// 黒
			back_g.drawRoundRect(MX1-MIS*2-FUKIW, FUKIY1, FUKIW, FUKIH, MIS*2, MIS*2);// フキダシ（角丸四角）表示
			back_g.setColor(hcolor);// ふきだしカラー
			back_g.fillPolygon(poly_x, poly_y, 3);// フキダシ（三角）表示
			back_g.setColor(Color.black);// 黒
			back_g.drawLine(poly_x[0], poly_y[0], poly_x[1], poly_y[1]);// フキダシ（線）表示
			back_g.drawLine(poly_x[0], poly_y[0], poly_x[2], poly_y[2]);// フキダシ（線）表示
			if (FpoiImg.elementAt(lobj_id).toString().length() > 0) {
				if (Fpoi[lobj_id].img == null) {// 未ロードならロードする
					Fpoi[lobj_id].img = LoadImage(FpoiImg.elementAt(lobj_id).toString());
				}
				back_g.drawImage(Fpoi[lobj_id].img, MX1-MIS*2-FUKIW+MIS, FUKIY1+MIS, FUKIW-MIS*2, FUKIW-MIS*2, this);// 建物画像表示
			}
			if (FpoiTxt.elementAt(lobj_id).toString().length() > 0) {
				if (Fpoi[lobj_id].txt == null) {// 未ロードならロードする
					Fpoi[lobj_id].txt = LoadText(FpoiTxt.elementAt(lobj_id).toString());
				}
				if (!textArea1.isVisible()) textArea1.setVisible(true);
				textArea1.setText(Fpoi[lobj_id].txt);// 建物説明表示
			}
		} else {
			if (textArea1.isVisible()) textArea1.setVisible(false);
		}

		}// end of paintMap()

		public void paintPano(Graphics2D g2) {

		// パノラマ／パタパタを描画
		String charahuki = "";
		String label2text = "";
		if (pata_mode > 0) {// 
			if (task1 != null) {
				if (SCBtn.getLabel().equals(SCBTNLABEL3)) {
					if (pata_mode == 3 && !scenemsg1.equals("")) {// 回転モード
						label2text = scenemsg1;// シナリオ回転中メッセージを表示
						charahuki = scenemsg1;// シナリオ回転中メッセージを表示
					} else if (pata_mode == 2 && !scenemsg2.equals("")) {// パタパタモード
						label2text = scenemsg2;// シナリオ直進中メッセージを表示
						charahuki = scenemsg2;// シナリオ直進中メッセージを表示
					} else {
//						label2text = "シナリオ実行中。";
					}
				}
			} else {
//				label2text = "シナリオ一時停止中。";
				if (pata_mode == 3 && !scenemsg1.equals("")) {// 回転モード
					charahuki = scenemsg1;// シナリオ回転中メッセージを表示
				} else if (pata_mode == 2 && !scenemsg2.equals("")) {// パタパタモード
					charahuki = scenemsg2;// シナリオ直進中メッセージを表示
				} else if (pata_mode == 4) {// 一時停止準備モード
					if (!scenemsg2.equals("")) {
						charahuki = scenemsg2;// シナリオ直進中メッセージを表示
					} else if (!scenemsg1.equals("")) {
						charahuki = scenemsg1;// シナリオ回転中メッセージを表示
					}
					scenemsg1 = "";
					scenemsg2 = "";
					pata_mode = 5;
				} else if (pata_mode == 5) {// 一時停止モード
				} else if (pata_mode == 6) {// 終了準備モード
					if (!scenemsg2.equals("")) {
						charahuki = scenemsg2;// シナリオ直進中メッセージを表示
					} else if (!scenemsg1.equals("")) {
						charahuki = scenemsg1;// シナリオ回転中メッセージを表示
					}
					scenemsg1 = "";
					scenemsg2 = "";
					pata_mode = 0;
				}
			}
		}
		// 道案内ガイドを描画
		if (disptype == 3) {// 道案内レイアウト
			back_g.drawImage(img_guide, MX1-MIS*6-FUKIW-150, MAPY1-ICONSIZE, 118, 280, this);// ガイド表示
		}
		// 道案内ふきだしを描画
		if (disptype == 3 && charahuki.length() > 0) {// 道案内レイアウト
			int len = charahuki.length();
			AttributedString astr = new AttributedString(charahuki);
			astr.addAttribute(TextAttribute.FONT, new Font("MS Gothic", Font.PLAIN, 20), 0, len);
			FontRenderContext fcontext = back_g.getFontRenderContext();
			AttributedCharacterIterator iter = astr.getIterator();
			LineBreakMeasurer lbreak = new LineBreakMeasurer(iter,fcontext);

			TextLayout layout;
			float x = MX1-MIS*6-FUKIW+MIS;
			float y = MAPY1+MIS;
			int width = FUKIW-MIS*2;
			if (lbreak != null) {
				lbreak.setPosition(0);
				while (lbreak.getPosition() < len) {// 各行についてループする
					layout = lbreak.nextLayout(width);// この行に表示する文字列のLayoutを入手する
					y += layout.getAscent() + layout.getDescent();// 表示場所を１行下げる
				}

				int poly_x[] = {MX1-MIS*6-FUKIW-40, MX1-MIS*6-FUKIW+1, MX1-MIS*6-FUKIW+1};
				int poly_y[] = {MAPY1+42, MAPY1+34, MAPY1+14};
				back_g.setColor(hcolor);// ふきだしカラー
				back_g.fillRoundRect(MX1-MIS*6-FUKIW, MAPY1, FUKIW, (int)y-MAPY1+MIS, MIS*2, MIS*2);// フキダシ（角丸四角）表示
				back_g.setColor(Color.black);// 黒
				back_g.drawRoundRect(MX1-MIS*6-FUKIW, MAPY1, FUKIW, (int)y-MAPY1+MIS, MIS*2, MIS*2);// フキダシ（角丸四角）表示
				back_g.setColor(hcolor);// ふきだしカラー
				back_g.fillPolygon(poly_x, poly_y, 3);// フキダシ（三角）表示
				back_g.setColor(Color.black);// 黒
				back_g.drawLine(poly_x[0], poly_y[0], poly_x[1], poly_y[1]);// フキダシ（線）表示
				back_g.drawLine(poly_x[0], poly_y[0], poly_x[2], poly_y[2]);// フキダシ（線）表示

				y = MAPY1+MIS;
				lbreak.setPosition(0);
				while (lbreak.getPosition() < len) {// 各行についてループする
					layout = lbreak.nextLayout(width);// この行に表示する文字列のLayoutを入手する
					y += layout.getAscent() + layout.getDescent();// 表示場所を１行下げる
					layout.draw(back_g, x, y);
				}
			}
			label2text = "";
		}
		if (pata_mode != 2) {// パノラマを描画
//System.out.print("["+PanoId.elementAt(point)+","+muki+"]");
			int prw = PANOW*100/panorate;// 表示サイズに表示可能なパノラマサイズ
			int prh = PANOH*100/panorate;// 表示サイズに表示可能なパノラマサイズ
			int pnw = pano_w*panorate/100;// 必要な表示サイズ
			int pnh = pano_h*panorate/100;// 必要な表示サイズ
			int pnm = muki*panorate/100;// 必要な表示サイズ(0～muki)
			int py1 = Math.max(PANOY1, PANOY1+(PANOH-pnh)/2);// 表示位置ｙ１
			int py2 = Math.min(PANOY1+PANOH, py1 + pnh);// 表示位置ｙ２
			int qy1 = Math.max(0, (pano_h-prh)/2);// 表示パノラマｙ１
			int qy2 = Math.min(pano_h, qy1 + prh);// 表示パノラマｙ２
			if (muki < prw/2) {
				back_g.drawImage(Pano[point].img, PX1, py1, PMX-pnm, py2, pano_w+muki-prw/2, qy1, pano_w, qy2, this);// 不透明画像表示
				back_g.drawImage(Pano[point].img, PMX-pnm, py1, PX2, py2, 0, qy1, muki+prw/2, qy2, this);// 不透明画像表示
			} else if (muki < pano_w-prw/2) {
				back_g.drawImage(Pano[point].img, PX1, py1, PX2, py2, muki-prw/2, qy1, muki+prw/2, qy2, this);// 不透明画像表示
			} else if (muki < pano_w) {
				back_g.drawImage(Pano[point].img, PX1, py1, PMX+pnw-pnm, py2, muki-prw/2, qy1, pano_w, qy2, this);// 不透明画像表示
				back_g.drawImage(Pano[point].img, PMX+pnw-pnm, py1, PX2, py2, 0, qy1, muki-pano_w+prw/2, qy2, this);// 不透明画像表示
			}
			back_g.setStroke(stroke2);
			for (int i=0; i<links[point].linkSet.area.length; i++){// 枠表示ループ
				LinkArea la = links[point].linkSet.area[i];
				int dx1 = dispX(pano_w, la.sx);
				int dx2 = dispX(pano_w, la.ex);
				int dy1 = dispY(pano_h, la.sy);
				int dy2 = dispY(pano_h, la.ey);
				if (dy1 == -1 && dy2 >= PANOY1) dy1 = PANOY1;
				if (dy2 == PANOY1+PANOH+1 && dy1 < PANOY1+PANOH) dy2 = PANOY1+PANOH-1;
				if (waku_mode == 0 && i != waku_no) {// 枠非表示？
					dy1 = py2 - 2;
					dy2 = py2 - 1;
				}
				if ((dx1 >= 0 || dx2 >= 0) && (dy1 >= PANOY1 && dy1 <= PANOY1+PANOH) && (dy2 >= PANOY1 && dy2 <= PANOY1+PANOH+1)) {// 表示範囲内
					if (dx1 < 0) dx1 = PX1;
					if (dx2 < 0) dx2 = PX2-1;
					if (task1 == null && pata_mode != 3 && la.id >= 0 && la.id <= LINKIDMIN-1) {// パタパタへのリンク
						back_g.setColor(Color.red);// 赤
						back_g.drawRect(dx1, dy1, dx2-dx1, dy2-dy1); // 四角
					} else if (la.id >= LINKIDMIN && la.id <= WARPIDMIN-1) {// オブジェクトへのリンク
						back_g.setColor(Color.blue);// 青
						back_g.drawRect(dx1, dy1, dx2-dx1, dy2-dy1); // 四角
					} else if (task1 == null && pata_mode != 3 && la.id >= WARPIDMIN) {// ワープ
						back_g.setColor(Color.yellow);// 黄
						back_g.drawRect(dx1, dy1, dx2-dx1, dy2-dy1); // 四角
					}
				}
			}
			back_g.setStroke(stroke1);
			if (waku_no >= 0) {// 枠説明表示？
				LinkArea la = links[point].linkSet.area[waku_no];
				if (la.id >= 0 && la.id <= LINKIDMIN-1) {// パタパタへのリンク
					label2text = "この方向へ進みます。";
//					label2text = "この方向へ進みます。["+PanoId.elementAt(la.id)+"]";
				} else if (la.id >= LINKIDMIN && la.id <= WARPIDMIN-1) {// オブジェクトへのリンク
					label2text = "関連する情報を表示します。";
//					label2text = "関連する情報を表示します。["+LinkObj[la.data1]+"]";
				} else {// ワープ
					label2text = "別の場所へジャンプします。";
//					label2text = "別の場所へジャンプします。["+LinkObj[la.data1]+":"+la.data3+"]";
				}
			}
			if (button_no > 0) {// ボタン説明表示？
				label2text = BTNMSG[button_no];
			}
			// パノラマ枠を描画
			back_g.setColor(wcolor);// ワクカラー

			back_g.fill3DRect(PX1-ICONSIZE, PANOY1+PANOH, ICONSIZE, ICONSIZE, true); // パノラマ左下四角
			back_g.drawImage(img_arwicon, PX1-ICONSIZE, PANOY1+PANOH, PX1, PANOY1+PANOH+ICONSIZE, 0, 60, 16, 76, this);// 縮小アイコン画像表示
			back_g.fill3DRect(PX2, PANOY1+PANOH, ICONSIZE, ICONSIZE, true); // パノラマ右下四角
			back_g.drawImage(img_arwicon, PX2, PANOY1+PANOH, PX2+ICONSIZE, PANOY1+PANOH+ICONSIZE, 16, 60, 32, 76, this);// 拡大アイコン画像表示

			back_g.fill3DRect(PX1-MIS, PANOY1-MIS, PANOW+MIS*2, MIS, true); // パノラマ上四角
			back_g.setClip(PX1-1, PANOY1+PANOH, PANOW+2, MIS);// クリップ領域を指定
			back_g.fill3DRect(PX1-MIS, PANOY1+PANOH, PANOW+MIS*2, MIS, true); // パノラマ下四角
			back_g.setClip(PX1-MIS, PANOY1-1, MIS, PANOH+2);// クリップ領域を指定
			back_g.fill3DRect(PX1-MIS, PANOY1-2, MIS, PANOH+4, true); // パノラマ左四角
			back_g.setClip(PX2, PANOY1-1, MIS, PANOH+2);// クリップ領域を指定
			back_g.fill3DRect(PX2, PANOY1-2, MIS, PANOH+4, true); // パノラマ右四角

			back_g.setClip(PX1-10-ARWW1-10-ARWW2-5, PMY-ARWH/2-5, 5+ARWW2+10+ARWW1+1, ARWH+10);// クリップ領域を指定
			back_g.fill3DRect(PX1-10-ARWW1-10-ARWW2-5, PMY-ARWH/2-5, 5+ARWW1+10+ARWW2+2, ARWH+10, true); // パノラマ左四角
			back_g.setClip(PX2+10-1, PMY-ARWH/2-5, 1+ARWW1+10+ARWW2+5, ARWH+10);// クリップ領域を指定
			back_g.fill3DRect(PX2+10-2, PMY-ARWH/2-5, 2+ARWW1+10+ARWW2+5, ARWH+10, true); // パノラマ右四角
			back_g.setClip(null);// クリップ領域を解除
			if (rota_mode == 1 || rota_mode == 11) {
				back_g.drawImage(img_arwicon, PX1-10-ARWW1, PMY-ARWH/2, PX1-10, PMY+ARWH/2, 0, 30, 40, 60, this);// 左矢印画像表示
			} else {
				back_g.drawImage(img_arwicon, PX1-10-ARWW1, PMY-ARWH/2, PX1-10, PMY+ARWH/2, 0, 0, 40, 30, this);// 左矢印画像表示
			}
			if (rota_mode == 2 || rota_mode == 12) {
				back_g.drawImage(img_arwicon, PX2+10, PMY-ARWH/2, PX2+10+ARWW1, PMY+ARWH/2, 40, 30, 80, 60, this);// 右矢印画像表示
			} else {
				back_g.drawImage(img_arwicon, PX2+10, PMY-ARWH/2, PX2+10+ARWW1, PMY+ARWH/2, 40, 0, 80, 30, this);// 右矢印画像表示
			}
			if (rota_mode == 3 || rota_mode == 13) {
				back_g.drawImage(img_arwicon, PX1-10-ARWW1-10-ARWW2, PMY-ARWH/2, PX1-10-ARWW1-10, PMY+ARWH/2, 0, 30, 40, 60, this);// 左矢印画像表示
			} else {
				back_g.drawImage(img_arwicon, PX1-10-ARWW1-10-ARWW2, PMY-ARWH/2, PX1-10-ARWW1-10, PMY+ARWH/2, 0, 0, 40, 30, this);// 左矢印画像表示
			}
			if (rota_mode == 4 || rota_mode == 14) {
				back_g.drawImage(img_arwicon, PX2+10+ARWW1+10, PMY-ARWH/2, PX2+10+ARWW1+10+ARWW2, PMY+ARWH/2, 40, 30, 80, 60, this);// 右矢印画像表示
			} else {
				back_g.drawImage(img_arwicon, PX2+10+ARWW1+10, PMY-ARWH/2, PX2+10+ARWW1+10+ARWW2, PMY+ARWH/2, 40, 0, 80, 30, this);// 右矢印画像表示
			}
			label1.setText("["+PanoId.elementAt(point)+","+muki+"]");
		} else {// パタパタを描画
//System.out.print("("+pata_now+")");
			if (PataImg[pata_now] == null) {// 未ロードならロードする
				PataImg[pata_now] = LoadImage(PataUri.elementAt(pata_now).toString());
			}
			int tw = PataImg[pata_now].getWidth(this);
			int th = PataImg[pata_now].getHeight(this);
			int py1 = Math.max(PANOY1, PMY-th/2);// 表示位置ｙ１
			int py2 = Math.min(PANOY1+PANOH, py1 + th);// 表示位置ｙ２
			int qy1 = Math.max(0, (th-PANOH)/2);// 表示パタパタｙ１
			int qy2 = Math.min(th, qy1 + PANOH);// 表示パタパタｙ２
			if (tw >= PANOW) {
				back_g.drawImage(PataImg[pata_now], PX1, py1, PX2, py2, (tw-PANOW)/2, qy1, (tw+PANOW)/2, qy2, this);// 不透明画像表示
			} else {
				back_g.drawImage(PataImg[pata_now], PMX-tw/2, py1, PMX+tw/2, py2, 0, qy1, tw, qy2, this);// 不透明画像表示
			}
			label1.setText("("+pata_now+")");
			// パノラマ枠を描画
			back_g.setColor(wcolor);// ワクカラー
			back_g.fill3DRect(PX1-MIS, PANOY1-MIS, PANOW+MIS*2, MIS, true); // パノラマ上四角
			back_g.fill3DRect(PX1-MIS, PANOY1+PANOH, PANOW+MIS*2, MIS, true); // パノラマ下四角
			back_g.setClip(PX1-MIS, PANOY1-1, MIS, PANOH+2);// クリップ領域を指定
			back_g.fill3DRect(PX1-MIS, PANOY1-2, MIS, PANOH+4, true); // パノラマ左四角
			back_g.setClip(PX2, PANOY1-1, MIS, PANOH+2);// クリップ領域を指定
			back_g.fill3DRect(PX2, PANOY1-2, MIS, PANOH+4, true); // パノラマ右四角
			back_g.setClip(null);// クリップ領域を解除
		}
		label2.setText(label2text);

		}// end of paintPano()

		public void update (Graphics g) {
//			back_g.clearRect(0,0,getSize().width,getSize().height);
			paint(g);
		}
	}

	// 地図の表示中心からの相対位置ｘを求める
	public int Calc_ddx(int x, int y) {
		return (int)((Math.cos(mapa*Math.PI/180.0)*(x-mapx)-Math.sin(mapa*Math.PI/180.0)*(y-mapy))*maprate/100);
	}

	// 地図の表示中心からの相対位置ｙを求める
	public int Calc_ddy(int x, int y) {
		return (int)((Math.sin(mapa*Math.PI/180.0)*(x-mapx)+Math.cos(mapa*Math.PI/180.0)*(y-mapy))*maprate/100);
	}

	// 画像を読み込む
	public Image LoadImage(String fname) {
		Image img = getImage(getCodeBase(), fname);
		try {
			mt.addImage(img, mtid);
			mt.waitForID(mtid);
			mtid++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	// テキストを読み込む
	public String LoadText(String fname) {
		String txt = "";
		if (fname.length() == 0) return txt;
		try {
			URL mobjurl = new URL(getCodeBase().toString()+fname);
			BufferedReader reader = new BufferedReader(new InputStreamReader(mobjurl.openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				txt = txt + line + "\r\n";
			}
		} catch (FileNotFoundException e){
			System.out.println("File not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O Error!");
			e.printStackTrace();
		}
		return txt;
	}

	public void PanoInit(int newpoint) {
		point = newpoint;
		if (Pano[point].img == null) {// 未ロードならロードする
			Pano[point].img = LoadImage(PanoUri.elementAt(point).toString());
		}
		pano_w = Pano[point].img.getWidth(this);
		pano_a = Pano[point].angle;
		if (cursor == 1) {
			cursor = 0;
			canvas1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		waku_no = -1;
	}

	public void tm1start() {
		if (task1 == null) {
			task1 = new TimerTask1();
		}
		tm1.schedule(task1, 500, period1);
	}

	public void tm2start() {
		if (task2 == null) {
			task2 = new TimerTask2();
		}
		tm2.schedule(task2, DEF_DELAY2, DEF_PERIOD2);
	}

	public void tm2start(long delay, long period) {
		if (task2 == null) {
			task2 = new TimerTask2();
		}
		tm2.schedule(task2, delay, period);
	}

	public void tm1stop() {
		if (task1 != null) {
			task1.cancel();
			task1 = null;
		}
	}

	public void tm2stop() {
		if (task2 != null) {
			task2.cancel();
			task2 = null;
		}
	}

	class TimerTask1 extends TimerTask {
		public void run() {
			action_tm1();
		}
	}

	class TimerTask2 extends TimerTask {
		public void run() {
			action_tm2();
		}
	}

	public void action_tm2() {
		if (rota_mode > 0 && rota_mode <= 10) {//回転モード
			rota_mode = rota_mode + 10;
		}
		if (rota_mode > 10) {//回転モード
			muki = (muki+d_muki+pano_w) % pano_w;
			canvas1.repaint();
		}
		if (map_mode == 1 || map_mode == 11) {//地図操作モード（移動）
			map_mode = 11;
			mapx = Math.min(Math.max(mapx + d_mapx, MAPW/2*100/maprate), mapw-MAPW/2*100/maprate);
			mapy = Math.min(Math.max(mapy + d_mapy, MAPH/2*100/maprate), maph-MAPH/2*100/maprate);
			canvas1.repaint();
		} else if (map_mode == 2 || map_mode == 12) {//地図操作モード（拡大／縮小）
			map_mode = 12;
			if (d_maprate < 0 && maprate > MAP_ZOOM_MIN || d_maprate > 0 && maprate < MAP_ZOOM_MAX) {
				maprate = maprate + (maprate+1)/d_maprate;
				mpps = MPPS - Math.max(0, (100-maprate)/25);
				mois = MOIS - Math.max(0, (100-maprate)/5);
				fitMap2();
				canvas1.repaint();
			}
		} else if (map_mode == 3 || map_mode == 13) {//地図操作モード（回転）
			map_mode = 13;
			mapa = (mapa+d_mapa) % 360;
			canvas1.repaint();
		} else if (map_mode == 4 || map_mode == 14) {//パノラマ操作モード（拡大／縮小）
			map_mode = 14;
			if (d_panorate < 0 && panorate > PANO_ZOOM_MIN || d_panorate > 0 && panorate < PANO_ZOOM_MAX) {
				panorate = panorate + (panorate+1)/d_panorate;
				canvas1.repaint();
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object == btn1) { // ボタンクリックイベント
			ReadData();
			canvas1.repaint();
		} else if (object == btn2) { // ボタン（初期位置）クリックイベント
			PanoInit(initpoint);
			muki = initmuki;
			canvas1.repaint();
		} else if (object == SRBtn) { // ボタン（シナリオ読込）クリックイベント
			ReadScenario();
			canvas1.repaint();
		} else if (object == SSBtn) { // ボタン（シナリオ開始／再開／一時停止）クリックイベント
			if (SSBtn.getLabel().equals(SSBTNLABEL1)) {// シナリオ開始
				action_SSBtn_1();
			} else if (SSBtn.getLabel().equals(SSBTNLABEL2)) {// 再開
				if (pata_mode == 0 || pata_mode == 4 || pata_mode == 5 || pata_mode == 6) {
					action_SSBtn_1();
				} else {
					tm1start();
					SSBtn.setLabel(SSBTNLABEL3);
					SCBtn.setLabel(SCBTNLABEL3);
				}
			} else if (pata_mode != 0 && SSBtn.getLabel().equals(SSBTNLABEL3)) {// 一時停止
				tm1stop();
				SSBtn.setLabel(SSBTNLABEL2);
				SCBtn.setLabel(SCBTNLABEL2);
				canvas1.repaint();
			}
		} else if (object == SCBtn) { // ボタン（コマ送り／中止）クリックイベント
			if (SCBtn.getLabel().equals(SCBTNLABEL3)) {// 中止
				scenemsg1 = "";
				scenemsg2 = "";
				pata_mode = 0;
				tm1stop();
				scene = scene.delete(0,scene.length());
				SSBtn.setLabel(SSBTNLABEL1);
				SCBtn.setLabel(SCBTNLABEL1);
				canvas1.repaint();
			} else if (SCBtn.getLabel().equals(SCBTNLABEL1)) {// コマ送り
				if (pata_mode == 0 || pata_mode == 4 || pata_mode == 5 || pata_mode == 6) {
					action_SSBtn_1();
					tm1stop();
					SSBtn.setLabel(SSBTNLABEL2);
					SCBtn.setLabel(SCBTNLABEL2);
					action_tm1();
				} else {
					action_tm1();
				}
			}
		}
	}

	public void action_tm1() {
		if (pata_mode == 1) {// パタパタ準備モード
			pata_mode = 2;// パタパタモード
			norepaint = 0;
			action_tm1_1();
		} else if (pata_mode == 2) {// パタパタモード
			action_tm1_1();
		} else if (pata_mode == 3) {// パノラマで自動回転
			action_tm1_2();
		}
	}

	public void action_tm1_1() {// パタパタモード
		if (pata_now >= 0 && PataImg[pata_now] != null) {
			PataImg[pata_now].flush();// リソース解放
			PataImg[pata_now] = null;
		}
		if (pata_now < pata_to) {
			pata_now = pata_now + 1;
			canvas1.repaint();
			if (pata_now < pata_to && PataImg[pata_now+1] == null) {// パタパタの先読みが必要か？
				PataImg[pata_now+1] = LoadImage(PataUri.elementAt(pata_now+1).toString());
			}
		} else if (scene.length()==0) {// シナリオなし
			pata_mode = 6;// 終了準備モード（１回だけ説明表示し、pata_mode = 0 となる）
			tm1stop();
			SSBtn.setLabel(SSBTNLABEL1);
			SCBtn.setLabel(SCBTNLABEL1);
			canvas1.repaint();
		} else if (task1 != null && SSBtn.getLabel().equals(SSBTNLABEL2)) {
			pata_mode = 4;// 一時停止準備モード
			tm1stop();
			canvas1.repaint();
		} else {// シナリオが残っている
//System.out.print("<"+scene.toString()+">");
			String nextstr = "";
			int nextpid = -1;
			int c = scene.toString().indexOf(';');
			if (c == -1) {// １つ残っている
				nextstr = scene.toString();
				scene = scene.delete(0,scene.length());
			} else {// 複数残っている
				nextstr = scene.substring(0,c);
				scene = scene.delete(0,c+1);
			}
			int c1 = nextstr.lastIndexOf(',');
			scenemsg2 = nextstr.substring(c1+1);// シナリオ直進中メッセージ
			nextstr = nextstr.substring(0,c1);
			c1 = nextstr.lastIndexOf(',');
			scenemsg1 = nextstr.substring(c1+1);// シナリオ回転中メッセージ
			nextstr = nextstr.substring(0,c1);
			if (nextstr.equals("stop")) {// 途中でストップする
				System.out.println("Stop!!");
				pata_mode = 4;// 一時停止準備モード
				tm1stop();
				SSBtn.setLabel(SSBTNLABEL2);
				SCBtn.setLabel(SCBTNLABEL2);
			} else {
//				nextpid = Integer.parseInt(nextstr);
				nextpid = getId(nextstr);// ＩＤ番号を取得
				if (nextpid < 0) {// エラー
					System.out.println("ＩＤ:" + nextstr + "が不明です");
					pata_mode = 0;
					tm1stop();
					SSBtn.setLabel(SSBTNLABEL1);
					SCBtn.setLabel(SCBTNLABEL1);
					canvas1.repaint();
					return;
				}
				pata_index = getAreaIndex(nextpid);// 領域インデックスを取得
				pata_muki = getMuki(pata_index, 0);// 目標向きを取得
				pata_mode = 3;// 回転モード
				if (pata_index < 0) {// エラー
					pata_mode = 0;
					tm1stop();
					SSBtn.setLabel(SSBTNLABEL1);
					SCBtn.setLabel(SCBTNLABEL1);
				}
			}
			canvas1.repaint();
		}
	}

	public void action_tm1_2() {// パノラマで自動回転
		if (muki == pata_muki) {// 目標向きに達した！
			if (links[point].linkSet.area[pata_index].id < LINKIDMIN) {// パノラマＩＤ
				if (links[point].linkSet.area[pata_index].data2 > 0) {// パタパタが１枚以上ある
					pata_from = links[point].linkSet.area[pata_index].data1;
					pata_now = pata_from;
					pata_to = pata_now + links[point].linkSet.area[pata_index].data2 - 1;
					pata_mode = 2;// パタパタモード
					if (PataImg[pata_now] == null) {// 未ロードならロードする
						PataImg[pata_now] = LoadImage(PataUri.elementAt(pata_now).toString());
					}
				} else {
					pata_mode = 1;// パタパタ準備モード
				}
				backpoint = point;
				backmuki = muki;
				PanoInit(links[point].linkSet.area[pata_index].id);
				pata_index = getAreaIndex(backpoint);// 領域インデックスを取得
				muki = getMuki(pata_index, 1);// 逆向きを取得
			} else if (links[point].linkSet.area[pata_index].id >= WARPIDMIN) {// ワープＩＤ
				norepaint = 1;
				int lo = links[point].linkSet.area[pata_index].data1;
				String data3 = links[point].linkSet.area[pata_index].data3;
				String data4 = links[point].linkSet.area[pata_index].data4;
				choiceMapxml.select(getMapxmlIndex(LinkObj[lo]));
				ReadData();
				if (!data3.equals("")) {// 初期ＩＤあり？
					int id = getId(data3);// ＩＤ番号を取得
					if (id >= 0) {
						PanoInit(id);
						backpoint = point;
					} else {
						System.out.println("初期ＩＤ:" + data3 + "が不明です");
					}
					if (!data4.equals("")) {// 初期向きあり？
						int gyaku = 0;
						if (data4.startsWith("!")) {// 逆向き？
							data4 = data4.substring(1);
							gyaku = 1;
						}
						int id2 = getId(data4);// ＩＤ番号を取得
						if (id2 >= 0) {
							muki = getMuki(getAreaIndex(id2), gyaku);// 向きを取得する
						} else {
							System.out.println("初期向き:" + data4 + "が不明です");
						}
					}
				}
				pata_now = 0;
				pata_to = 0;
				norepaint = 0;
				pata_mode = 1;// パタパタ準備モード
			} else {
				pata_mode = 2;// パタパタモード
				action_tm1_1();
			}
		} else {
			int dmuki = (pata_muki-muki+pano_w) % pano_w;
			if (dmuki <= pano_w/2) {// 右回り
				if (dmuki < pano_w/24) {
					muki = pata_muki;
				} else {
					muki = (muki + pano_w/24) % pano_w;
				}
			} else {//左回り
				if (dmuki > pano_w - pano_w/24) {
					muki = pata_muki;
				} else {
					muki = (muki - pano_w/24 + pano_w) % pano_w;
				}
			}
		}
		canvas1.repaint();
	}

	public void action_SSBtn_1() {
		if (disptype == 2) {// 地図レイアウト→道案内レイアウト
			disptype = 3;
			initSize();
			initLayout();
			fitMap1();
		}
		if (scene.length() == 0) {// シナリオがない
			if (choScena.getSelectedIndex() >= 0) {// シナリオが選択されている
				scene.append((String)Scene.elementAt(choScena.getSelectedIndex()));
			}
		}
		if (scene.length() > 0) {// シナリオがある
//System.out.print("<"+scene.toString()+">");
			String nextstr = "";
			int nextpid = -1;
			int c = scene.toString().indexOf(';');
			if (c == -1) {// １つ残っている
				nextstr = scene.toString();
				scene = scene.delete(0,scene.length());
			} else {// 複数残っている
				nextstr = scene.substring(0,c);
				scene = scene.delete(0,c+1);
			}
			int c1 = nextstr.lastIndexOf(',');
			scenemsg2 = nextstr.substring(c1+1);// シナリオ直進中メッセージ
			nextstr = nextstr.substring(0,c1);
			c1 = nextstr.lastIndexOf(',');
			scenemsg1 = nextstr.substring(c1+1);// シナリオ回転中メッセージ
			nextstr = nextstr.substring(0,c1);
			if (nextstr.equals("stop")) {// 途中でストップする
				System.out.println("Stop!!");
				pata_mode = 4;// 一時停止準備モード
				tm1stop();
				SSBtn.setLabel(SSBTNLABEL1);
				SCBtn.setLabel(SCBTNLABEL1);
			} else {
//				nextpid = Integer.parseInt(nextstr);
				nextpid = getId(nextstr);// ＩＤ番号を取得
				if (nextpid < 0) {// エラー
					System.out.println("ＩＤ:" + nextstr + "が不明です");
					return;
				}
				pata_mode = 1;// パタパタ準備モード
				pata_now = 0;
				pata_to = 0;
				tm1start();// タイマースタート
				SSBtn.setLabel(SSBTNLABEL3);
				SCBtn.setLabel(SCBTNLABEL3);
				int pw = pano_w;
				int pa = pano_a;
				PanoInit(nextpid);
				muki = (int)(((muki*360+((double)pano_a-pa)*pw)*pano_w/360/pw) + pano_w) % pano_w;// 同じ向きにする
//				canvas1.repaint();
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		Object object = e.getSource();
		if (object == choScena) { // チョイス選択イベント
			canvas1.repaint();
		} else if (object == choSpeed) { // チョイス選択イベント
			int newspeed = choSpeed.getSelectedIndex();
			if (pata_speed != newspeed) {
				pata_speed = newspeed;
				if (pata_speed == 0) {// 遅い
					period1 = 1000;
				} else if (pata_speed == 1) {// 普通
					period1 = 500;
				} else if (pata_speed == 2) {// 速い
					period1 = 300;
				}
				if (task1 != null) {
					tm1stop();
					tm1start();
				}
			}
		} else if (object == chkWaku) { // チェックボックス（枠表示）クリックイベント
			waku_mode = chkWaku.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox1) { // チェックボックス（写真表示）クリックイベント
			map_imgmode = checkbox1.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox2) { // チェックボックス（中心固定）クリックイベント
			map_centermode = checkbox2.getState() ? 1 : 0;
			if (map_centermode == 0 || map_upmode == 0) {
				checkbox4.setVisible(false);
				map_popmode = 0;
			} else {
				checkbox4.setVisible(true);
				map_popmode = checkbox4.getState() ? 1 : 0;
			}
			canvas1.repaint();
		} else if (object == checkbox3) { // チェックボックス（上固定）クリックイベント
			map_upmode = checkbox3.getState() ? 1 : 0;
			if (map_centermode == 0 || map_upmode == 0) {
				checkbox4.setVisible(false);
				map_popmode = 0;
			} else {
				checkbox4.setVisible(true);
				map_popmode = checkbox4.getState() ? 1 : 0;
			}
			canvas1.repaint();
		} else if (object == checkbox4) { // チェックボックス（ふきだし）クリックイベント
			map_popmode = checkbox4.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox5) { // チェックボックス（シナリオ）クリックイベント
			map_scemode = checkbox5.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox6) { // チェックボックス（方角表示）クリックイベント
			map_dispamode = checkbox6.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox7) { // チェックボックス（経路表示）クリックイベント
			map_disprmode = checkbox7.getState() ? 1 : 0;
			canvas1.repaint();
		}
	}

	public void ReadInitdata() {
	try{
		DOMParser parser = new DOMParser();
		parser.parse(getCodeBase().toString()+INITDATA_XML);
		Document doc = parser.getDocument();
		Element rootnode = doc.getDocumentElement();
		Element element = null;// ワーク

		String name = "";// nameの値
		String file = "";// fileの値
		String key = "";// keyの値
		String value = "";// valueの値
		int id = 0;// ＩＤ
		int i = 0;// ループ用
		int j = 0;// ループ用
		// 地図データ情報を読む
		NodeList mapnodes = rootnode.getElementsByTagName("map");// mapノード検索
		int mapnum = mapnodes.getLength();
		for (i = 0; i < mapnum; i++) {// map数分ループ
			Element mapnode = (Element)mapnodes.item(i);// mapノード取得
			name = getChildNodeValue(mapnode, "name");// nameノードの値を（あれば）取得
			if (name.length() == 0) {
				System.out.println("Initdata read error : No map-name.");
				name = "name"+i;// nameの値を設定
			}
			choiceMapxml.addItem(name);
			file = getChildNodeValue(mapnode, "file");// fileノードの値を（あれば）取得
			if (file.length() == 0) {
				System.out.println("Initdata read error : No map-file.");
				file = "file"+i;// fileの値を設定
			}
			Mapxml.addElement(file);
		}
		// 条件データ情報を読む
		NodeList condsnodes = rootnode.getElementsByTagName("conds");// condsノード検索
		int condsnum = Math.min(condsnodes.getLength(), CONDS_MAX);// 条件数に制限あり
		for (i = 0; i < condsnum; i++) {// conds数分ループ
			Cond[i] = new Vector();
			Element condsnode = (Element)condsnodes.item(i);// condsノード取得
			key = getChildNodeValue(condsnode, "key");// keyノードの値を（あれば）取得
			if (key.length() == 0) {
				System.out.println("Initdata read error : No conds-key.");
				key = "key"+i;// keyの値を設定
			}
			Condkey[i] = key;
			if (disptype == 0 || disptype == 1) choiceCond[i].setVisible(true);
			NodeList condnodes = condsnode.getElementsByTagName("cond");// condノード検索
			int condnum = Math.min(condnodes.getLength(), COND_MAX);// 条件の項目数に制限あり
			for (j = 0; j < condnum; j++) {// cond数分ループ
				Element condnode = (Element)condnodes.item(j);// condノード取得
				name = getChildNodeValue(condnode, "name");// nameノードの値を（あれば）取得
				if (name.length() == 0) {
					System.out.println("Initdata read error : No cond-name.");
					name = "name"+j;// nameの値を設定
				}
				choiceCond[i].addItem(name);
				value = getChildNodeValue(condnode, "value");// valueノードの値を（あれば）取得
				if (value.length() == 0) {
					System.out.println("Initdata read error : No cond-value.");
					value = "value"+j;// valueの値を設定
				}
				Cond[i].addElement(value);
			}
		}
		for (; i < CONDS_MAX; i++) {// CONDS_MAX数分ループ
			Cond[i] = new Vector();
			Condkey[i] = "";
			choiceCond[i].setVisible(false);
		}
	} catch (Exception e) {
		e.printStackTrace();
		System.exit(1);
	}
	}

	public void ReadScenario() {
		String filename = "scenario.xml";// シナリオＸＭＬファイル名を取得
		ReadScenarioXMLFile(filename);
	}

	public void ReadScenarioXMLFile(String filename) {
	try{
		DOMParser parser = new DOMParser();
		parser.parse(getCodeBase().toString()+filename);
		Document doc = parser.getDocument();
		Element rootnode = doc.getDocumentElement();
		ReadScenarioElement(rootnode);
	} catch (Exception e) {
		e.printStackTrace();
		System.exit(1);
	}
	}

	public void ReadScenarioElement(Element rootnode) {
	try{
		Element element = null;// ワーク
		Scene = new Vector();
		String name = "";// シナリオ名
		String mode = "";// モード名
		String msg1 = "";// メッセージ１
		String msg2 = "";// メッセージ２
		String msg3 = "";// メッセージ３
		String value = "";// ワーク
		String value2 = "";// ワーク
		String value3 = "";// ワーク
		String lastid = "";// 直前ＩＤ
		int i = 0;// ループ用
		int j = 0;// ループ用
		// シナリオ情報を読む
		choScena.removeAll();// シナリオ選択ボックスをクリア
		NodeList scenenodes = rootnode.getElementsByTagName("scene");// シナリオ情報ノード検索
		int scenenum = scenenodes.getLength();
		for (i = 0; i < scenenum; i++) {// シナリオ情報数分ループ
			Element scenenode = (Element)scenenodes.item(i);// シナリオ情報ノード取得
			name = getChildNodeValue(scenenode, "name");// nameノードの値を（あれば）取得
			NodeList posnodes = scenenode.getElementsByTagName("pos");// posノード検索
			int posnum = posnodes.getLength();
			String scene = "";
			for (j = 0; j < posnum; j++) {// pos数分ループ
				element = (Element)posnodes.item(j);// posノード取得
				mode = element.getAttribute("mode");// mode属性値を取得
				if (element.hasAttribute("msg1")) {
					msg1 = element.getAttribute("msg1");// msg1属性値を取得
				} else {
					msg1 = "";
				}
				if (element.hasAttribute("msg2")) {
					msg2 = element.getAttribute("msg2");// msg2属性値を取得
				} else {
					msg2 = "";
				}
				if (element.hasAttribute("msg3")) {
					msg3 = element.getAttribute("msg3");// msg3属性値を取得
				} else {
					msg3 = "";
				}
				value = element.getFirstChild().getNodeValue();// posの値を取得

				value2 = "";
				value3 = "," + msg1 + "," + msg2;
				int c = value.indexOf(':');
				if (c > 0) {// ":"あり？
					value2 = value.substring(c+1);
					value = value.substring(0,c);
					value2 = ";" + value + ":" + value2 + "," + msg3 + ",";
					msg2 = "";
				}
				value3 = value + value3;
				if (!value.equals(lastid)) {// 直前と違うＩＤ
					value2 = ";" + value3 + value2;
				}
				if (j == 0 || mode.equals("start")) {// 最初またはmode=start
					scene = value3;
				} else if (mode.equals("stop")) {// mode=stop
					scene = scene + value2 + ";stop," + msg3 + "," + msg2 + ";" + value3;
//					scene = scene + value2 + ";stop,,";
				} else if (mode.equals("end")) {// mode=end
					scene = scene + value2;
					break;
				} else {// mode=skip
					scene = scene + value2;
				}
				lastid = value;
			}
			Scene.addElement(scene);
			choScena.addItem(name);
		}
		if (scenariono > 0 && scenariono < choScena.getItemCount()) {
			choScena.select(scenariono);
		}
		tfScena.setText(choScena.getSelectedItem());

	} catch (Exception e) {
		e.printStackTrace();
		System.exit(1);
	}
	}

	// 画像を読み込む
	void LoadMapfile() {
	try {
		if (img_map != null) {
			img_map.flush();
			img_map = null;
		}
		if (map_filename.length() > 0) {
			img_map = LoadImage(map_filename);
			mapw = img_map.getWidth(this);
			maph = img_map.getHeight(this);
			fitMap1();
		} else {
			mapw = 0;
			maph = 0;
			mapx = MAPW/2;
			mapy = MAPH/2;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

	void fitMap1() {
		if (disptype == 2 || disptype == 3) {// 地図レイアウトまたは道案内レイアウト
			maprate = MAPW*100/Math.max(mapw, maph);
			mpps = MPPS - Math.max(0, (100-maprate)/25);
			mois = MOIS - Math.max(0, (100-maprate)/5);
		}
		fitMap2();
	}

	void fitMap2() {
		if (mapw > MAPW*100/maprate) {
			mapx = Math.min(Math.max(mapx, MAPW/2*100/maprate), mapw-MAPW/2*100/maprate);
		} else {
			mapx = mapw/2;
		}
		if (maph > MAPH*100/maprate) {
			mapy = Math.min(Math.max(mapy, MAPH/2*100/maprate), maph-MAPH/2*100/maprate);
		} else {
			mapy = maph/2;
		}
	}

	public void ReadData() {
	try {
		String filename = Mapxml.elementAt(choiceMapxml.getSelectedIndex()).toString();// データファイル名を取得
		choScena.removeAll();// シナリオ選択ボックスをクリア
		Vector[] vi;
		v = new Vector();
		v = ReadXMLFile(filename);
		link = new LinkSet[v.size()];
		links = new LinkDataSet[v.size()];
		vi = new Vector[v.size()];
		v.copyInto(vi);
		for(int i=0;i<v.size();i++){
			//System.out.println("vi.size()="+vi[i].size());
			link[i] = new LinkSet(vi[i]);
			//System.out.println(toLinkObject.elementAt(i).toString());
			links[i] = new LinkDataSet(link[i],i,PanoUri.elementAt(i).toString());
		}
		// パノラマ情報を格納
		Pano = new Panorama[PanoUri.size()];
		for(int i=0;i<PanoUri.size();i++){
//			Image img = LoadImage(PanoUri.elementAt(i).toString());
			int mx = Integer.parseInt(PanoMx.elementAt(i).toString());
			int my = Integer.parseInt(PanoMy.elementAt(i).toString());
			int angle = Integer.parseInt(PanoAngle.elementAt(i).toString());
			Pano[i] = new Panorama(i, null, mx, my, angle, 0);
		}
		// パタパタ情報を格納
		PataImg = new Image[PataUri.size()];
		for(int i=0;i<PataUri.size();i++){
			PataImg[i] = null;
//			PataImg[i] = LoadImage(PataUri.elementAt(i).toString());
		}
		// リンクオブジェクトを格納
		LinkObj = new String[LinkUri.size()];
		for(int i=0;i<LinkUri.size();i++){
			LinkObj[i] = LinkUri.elementAt(i).toString();
		}
		// 画像を読み込む
		LoadMapfile();
//		img_map = LoadImage(map_filename);
		mapw = img_map.getWidth(this);
		maph = img_map.getHeight(this);

//		mt.waitForAll();

		// ふきだしＰＯＩを格納
		Fpoi = new MapObj[FpoiMx.size()];
		for(int i=0;i<FpoiMx.size();i++){
			int mx = Integer.parseInt(FpoiMx.elementAt(i).toString());
			int my = Integer.parseInt(FpoiMy.elementAt(i).toString());
//			Image icon = LoadImage(FpoiIcon.elementAt(i).toString());
//			Image img = LoadImage(FpoiImg.elementAt(i).toString());
//			String txt = LoadText(FpoiTxt.elementAt(i).toString());
			Fpoi[i] = new MapObj(i, mx, my, null, null, null);
		}

		// 名前ＰＯＩを格納
		Npoi = new MapObj[NpoiMx.size()];
		for(int i=0;i<NpoiMx.size();i++){
			int mx = Integer.parseInt(NpoiMx.elementAt(i).toString());
			int my = Integer.parseInt(NpoiMy.elementAt(i).toString());
			Npoi[i] = new MapObj(i, mx, my, null, null, null);
		}

		if (initidparam.length() > 0) {
			initidstring = initidparam;
			initidparam = "";
		}
		if (initmukiparam.length() > 0) {
			initmukistring = initmukiparam;
			initmukiparam = "";
		}
		if (!filename.equals(mapxmlfilename)) {// マップＸＭＬが変更された
			initpoint = 0;
			if (!initidstring.equals("")) {// initidがある
				int id = getId(initidstring);// ＩＤ番号を取得
				if (id >= 0) {
					initpoint = id;
				} else {
					System.out.println("初期ＩＤ:" + initidstring + "が不明です");
				}
			}
			PanoInit(initpoint);
			backpoint = point;
			mapx = Pano[point].mx;
			mapy = Pano[point].my;
			fitMap2();

			initmuki = 120;
			if (!initmukistring.equals("")) {// initmukiがある
				int gyaku = 0;
				String initmukiid = initmukistring;
				if (initmukiid.startsWith("!")) {// 逆向き？
					initmukiid = initmukiid.substring(1);
					gyaku = 1;
				}
				int id2 = getId(initmukiid);// ＩＤ番号を取得
				if (id2 < 0) {
					id2 = getId(initidstring + ":" + initmukiid);// ＩＤ番号を取得
				}
				if (id2 >= 0) {
					initmuki = getMuki(getAreaIndex(id2), gyaku);// 向きを取得する
				} else {
					System.out.println("初期向き:" + initmukiid + "が不明です");
				}
			}
			muki = initmuki;
			backmuki = muki;
		} else {// マップＸＭＬが変更されなかった
			int pw = pano_w;
			int pa = pano_a;
			PanoInit(point);
			backpoint = point;
			muki = (int)(((muki*360+((double)pano_a-pa)*pw)*pano_w/360/pw) + pano_w) % pano_w;// 同じ向きにする
			backmuki = muki;
		}

		mapxmlfilename = filename;

//	} catch (InterruptedException e) {
//		e.printStackTrace();
//	} catch (FileNotFoundException e){
//		System.out.println("File not found!");
//		e.printStackTrace();
//	} catch (IOException e) {
//		System.out.println("I/O Error!");
//		e.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

	public Vector ReadXMLFile(String filename) {
	try{
		DOMParser parser = new DOMParser();
		parser.parse(getCodeBase().toString()+filename);
		Document doc = parser.getDocument();
		Element rootnode = doc.getDocumentElement();
		Element element = null;// ワーク

		LinkArea linkArea;
		Vector v1 = new Vector();
		PanoUri = new Vector();
		PanoMx = new Vector();
		PanoMy = new Vector();
		PanoAngle = new Vector();
		PanoSkip = new Vector();
		PataUri = new Vector();
		LinkUri = new Vector();
		LinkId = new Vector();
		WarpId = new Vector();
		FpoiMx = new Vector();
		FpoiMy = new Vector();
		FpoiImg = new Vector();
		FpoiIcon = new Vector();
		FpoiTxt = new Vector();
		NpoiMx = new Vector();
		NpoiMy = new Vector();
		NpoiIcon = new Vector();
		String name = "";// シナリオ名
		String value = "";// ワーク
		String dir = "";
		String pre = "";
		String area = "";
		int type = 0;// Linkの場合(1:テキスト,2:画像,3:動画,4:HTML)
		int id = 0;// ＩＤ
		int c = 0;// カンマ位置
		int i = 0;// ループ用
		int j = 0;// ループ用
		int k = 0;// ループ用
		// パノラマ情報を読む（まずＩＤを設定）
		NodeList panonodes = rootnode.getElementsByTagName("pano");// パノラマノード検索
		int panonum = panonodes.getLength();
		PanoId = new Vector();
		for (i = 0; i < panonum; i++) {// パノラマ数分ループ
			Element panonode = (Element)panonodes.item(i);// パノラマノード取得
			value = panonode.getAttribute("id");// ＩＤを取得
			PanoId.addElement(value);// ＩＤを設定
		}
		// パノラマ情報を読む
		for (i = 0; i < panonum; i++) {// パノラマ数分ループ
			Vector PanoPoint = new Vector();
			Element panonode = (Element)panonodes.item(i);// パノラマノード取得
			NodeList mapnodes = panonode.getElementsByTagName("map");// mapノード検索
			int mapindex = 0;
			for (j = 0; j < mapnodes.getLength(); j++) {// map数分ループ
				element = (Element)mapnodes.item(j);// mapノード取得
				if (element.hasAttribute("map01")) {// 条件に一致した？
					mapindex = j;
					break;
				}
			}
			Element mapnode = (Element)mapnodes.item(mapindex);// mapノード取得
			value = getChildNodeValue(mapnode, "pos");// posノードの値を（あれば）取得
			c = value.indexOf(',');
			if (c < 0) {
				System.out.println("パノラマ:" + PanoId.elementAt(i).toString() + "内のposの記述に誤りがあります");
			}
			PanoMx.addElement(value.substring(0,c));
			PanoMy.addElement(value.substring(c+1));
			NodeList photonodes = panonode.getElementsByTagName("photo");// photoノード検索
			int photoindex = 0;
			double maxpoint = 0;
			double point = 0;
			for (j = 0; j < photonodes.getLength(); j++) {// photo数分ループ
				element = (Element)photonodes.item(j);// photoノード取得
				point = pointCond(element);// 条件を見て得点計算
				if (point > maxpoint) {// 最高得点を上回った
					photoindex = j;
					maxpoint = point;
				}
			}
			Element photonode = (Element)photonodes.item(photoindex);// photoノード取得

			value = getChildNodeValue(photonode, "panomuki");// panomukiノードの値を（あれば）取得
			if (value.length() == 0) {// panomukiの値がない→"0"を設定
				value = "0";
			}
			PanoAngle.addElement(value);

			dir = getChildNodeValue(photonode, "dir");// dirノードの値を（あれば）取得
			if (dir.length() > 0) {// dirの値がある→"\\"を追加する
				dir = dir + "\\";
			}
			value = getChildNodeValue(photonode, "file");// fileノードの値を（あれば）取得
			PanoUri.addElement(dir + value);

			NodeList patanodes = photonode.getElementsByTagName("pata");// パタパタノード検索
			NodeList linknodes = photonode.getElementsByTagName("link");// リンクノード検索
			NodeList warpnodes = photonode.getElementsByTagName("warp");// ワープノード検索

			for (j = 0; j < patanodes.getLength(); j++) {// パタパタ数分ループ
				Element patanode = (Element)patanodes.item(j);// パタパタノード取得
				value = patanode.getAttribute("toid");// toidの値を取得
				id = getId(value);// ＩＤ番号を取得
				if (id == -1) {
					System.out.println("パノラマ:" + PanoId.elementAt(i).toString() + "内のＩＤ:" + value + "が不明です");
					continue;
				}
				area = getChildNodeValue(patanode, "area");// areaノードの値を（あれば）取得
				c = area.indexOf(',');
				if (c < 0) {
					System.out.println("パノラマ:" + PanoId.elementAt(i).toString() + "内のareaの記述に誤りがあります");
				}
				int sx = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int sy = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int ex = Integer.parseInt(area.substring(0,c));
				int ey = Integer.parseInt(area.substring(c+1));
				int data1 = PataUri.size();// パタパタ開始番号
				int data2 = 0;// パタパタ数

				NodeList pathnodes = rootnode.getElementsByTagName("path");// pathノード検索
				int pathindex = 0;
				for (k = 0; k < pathnodes.getLength(); k++) {// path数分ループ
					element = (Element)pathnodes.item(k);// pathノード取得
					if (PanoId.elementAt(i).toString().equals(element.getAttribute("fromid")) &&
						value.equals(element.getAttribute("toid"))) {// ＩＤが一致した？
						pathindex = k;
						break;
					}
				}
				if (k >= pathnodes.getLength()) {
					System.out.println("path情報:"+PanoId.elementAt(i).toString()+"-"+value+"が不明です");
					continue;
				}
				Element pathnode = (Element)pathnodes.item(pathindex);// pathノード取得

				photonodes = pathnode.getElementsByTagName("photo");// photoノード検索
				photoindex = 0;
				maxpoint = 0;
				for (k = 0; k < photonodes.getLength(); k++) {// photo数分ループ
					element = (Element)photonodes.item(k);// photoノード取得
					point = pointCond(element);// 条件を見て得点計算
					if (point > maxpoint) {// 最高得点を上回った
						photoindex = k;
						maxpoint = point;
					}
				}
				photonode = (Element)photonodes.item(photoindex);// photoノード取得

				int photonum = 0;
				if (photonode.hasAttribute("num")) {// num属性がある
					photonum = Integer.parseInt(photonode.getAttribute("num"));// photo数取得
				}
				dir = getChildNodeValue(photonode, "dir");// dirノードの値を（あれば）取得
				if (dir.length() > 0) {// dirノードがある→"\\"を追加する
					dir = dir + "\\";
				}

				pre = getChildNodeValue(photonode, "pre");// preノードの値を（あれば）取得
				if (pre.length() == 0) {// preの値がない
					pre = pathnode.getAttribute("fromid") + "-" + pathnode.getAttribute("toid") + "-";// preを設定する（"fromid-toid-"）
				}
				for (k = 0; k < photonum; k++) {// photo数分ループ
					String strnum = "";
					if (k+1 < 10) {
						strnum = "0" + String.valueOf(k+1);
					} else {
						strnum = String.valueOf(k+1);
					}
					value = dir + pre + strnum + ".jpg";
					PataUri.addElement(value);
					data2++;
				}

				linkArea = new LinkArea(id, sx, sy, ex, ey, data1, data2, "", "");
				PanoPoint.addElement(linkArea);
			}

			for (j = 0; j < linknodes.getLength(); j++) {// リンク数分ループ
				Element linknode = (Element)linknodes.item(j);// リンクノード取得
				id = LINKIDMIN + LinkId.size();// リンク番号
				value = linknode.getAttribute("id");// ＩＤを取得
				LinkId.addElement(PanoId.elementAt(i).toString() + ":" + value);// ＩＤを設定
				value = getChildNodeValue(linknode, "object");// objectノードの値を（あれば）取得
				if (value.endsWith(".txt")) {
					type = 1;
				} else if (value.endsWith(".jpg") || value.endsWith(".gif")) {
					type = 2;
				} else if (value.endsWith(".mpg") || value.endsWith(".avi")) {
					type = 3;
				} else if (value.endsWith(".htm") || value.endsWith(".html")) {
					type = 4;
				} else if (value.endsWith(".xml")) {
					type = 11;
				} else {
					type = 99;
					System.out.println("未サポート:"+value);
				}
				area = getChildNodeValue(linknode, "area");// areaノードの値を（あれば）取得
				c = area.indexOf(',');
				int sx = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int sy = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int ex = Integer.parseInt(area.substring(0,c));
				int ey = Integer.parseInt(area.substring(c+1));
				int data1 = LinkUri.size();
				LinkUri.addElement(value);
				linkArea = new LinkArea(id, sx, sy, ex, ey, data1, type, "", "");
				PanoPoint.addElement(linkArea);
			}

			for (j = 0; j < warpnodes.getLength(); j++) {// ワープ数分ループ
				Element warpnode = (Element)warpnodes.item(j);// ワープノード取得
				id = WARPIDMIN + WarpId.size();// ワープ番号
				value = warpnode.getAttribute("id");// ＩＤを取得
				WarpId.addElement(PanoId.elementAt(i).toString() + ":" + value);// ＩＤを設定
				value = getChildNodeValue(warpnode, "object");// objectノードの値を（あれば）取得
				if (value.endsWith(".xml")) {
					type = 11;
				} else {
					type = 99;
					System.out.println("未サポート:"+value);
				}
				area = getChildNodeValue(warpnode, "area");// areaノードの値を（あれば）取得
				c = area.indexOf(',');
				int sx = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int sy = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int ex = Integer.parseInt(area.substring(0,c));
				int ey = Integer.parseInt(area.substring(c+1));
				int data1 = LinkUri.size();// オブジェクト番号
				LinkUri.addElement(value);

				String data3 = getChildNodeValue(warpnode, "initid");// initidノードの値を（あれば）取得
				String data4 = getChildNodeValue(warpnode, "initmuki");// initmukiノードの値を（あれば）取得
				linkArea = new LinkArea(id, sx, sy, ex, ey, data1, type, data3, data4);
				PanoPoint.addElement(linkArea);
			}

			v1.addElement(PanoPoint);
		}
		System.out.println("pano:"+panonum+",pata:"+PataUri.size());
		// ふきだしＰＯＩ／名前ＰＯＩを読む
		NodeList pointnodes = rootnode.getElementsByTagName("point");// ＰＯＩノード検索
		int pointnum = pointnodes.getLength();
		for (i = 0; i < pointnum; i++) {// ＰＯＩ数分ループ
			Element pointnode = (Element)pointnodes.item(i);// ＰＯＩノード取得
			value = pointnode.getAttribute("type");// typeを取得
			if (value.equals("2")) {// 名前ＰＯＩ
				value = getChildNodeValue(pointnode, "pos");// posノードの値を（あれば）取得
				c = value.indexOf(',');
				NpoiMx.addElement(value.substring(0,c));
				NpoiMy.addElement(value.substring(c+1));
				value = getChildNodeValue(pointnode, "icon");// iconノードの値を（あれば）取得
				NpoiIcon.addElement(value);
			} else {// ふきだしＰＯＩ
				value = getChildNodeValue(pointnode, "pos");// posノードの値を（あれば）取得
				c = value.indexOf(',');
				FpoiMx.addElement(value.substring(0,c));
				FpoiMy.addElement(value.substring(c+1));
				value = getChildNodeValue(pointnode, "icon");// iconノードの値を（あれば）取得
				FpoiIcon.addElement(value);
				value = getChildNodeValue(pointnode, "img");// imgノードの値を（あれば）取得
				FpoiImg.addElement(value);
				value = getChildNodeValue(pointnode, "txt");// txtノードの値を（あれば）取得
				FpoiTxt.addElement(value);
			}
		}
		// シナリオ情報を読む
		ReadScenarioElement(rootnode);
		// 地図情報を読む
		NodeList mapinfonodes = rootnode.getElementsByTagName("mapinfo");// 地図情報ノード検索
		if (mapinfonodes.getLength() > 0) {
			Element mapinfonode = (Element)mapinfonodes.item(0);// 地図情報ノード取得
			map_filename = getChildNodeValue(mapinfonode, "mapimg");// mapimgノードの値を（あれば）取得
			initidstring = getChildNodeValue(mapinfonode, "initid");// initidノードの値を（あれば）取得
			initmukistring = getChildNodeValue(mapinfonode, "initmuki");// initmukiノードの値を（あれば）取得
		} else {
			map_filename = "";
			initidstring = "";
			initmukistring = "";
		}

		return v1;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	}

	// 子ノードの値を（あれば）取得する
	public String getChildNodeValue(Element node, String childnodename) {
	try{
		String value = "";
		NodeList childnodes = node.getElementsByTagName(childnodename);// 子ノード検索
		if (childnodes.getLength() > 0) {// 子ノードがある？
			Node childnode = childnodes.item(0);
			if (childnode.hasChildNodes()) {// テキストノードがある？
				value = childnode.getFirstChild().getNodeValue();// 子ノードの値を取得
			}
		}
		return value;
	} catch (Exception e) {
		e.printStackTrace();
		return "";
	}
	}

	// 条件を見て得点計算する
	public double pointCond(Element element) {
		double point = 0;
		String cvalue;// 条件値
		String svalue;// 選択条件値
		for (int i=0; i<CONDS_MAX; i++) {
			if (Condkey[i]!=null && Condkey[i].length()>0 && element.hasAttribute(Condkey[i])) {// 条件がある？
				cvalue = element.getAttribute(Condkey[i]);// Condkey[i]の属性値を取得
				svalue = Cond[i].elementAt(choiceCond[i].getSelectedIndex()).toString();// 選択条件値を取得
				if (svalue.equals("default")) {// 条件="default"
					point = point + Math.pow(10, CONDS_MAX-1-i) * 9;// 9ポイント
				} else if (cvalue.equals(svalue)) {// 条件を比較→一致した
					point = point + Math.pow(10, CONDS_MAX-1-i) * 8;// 8ポイント
				} else {// 一致しない
					for (int j=0; j<Cond[i].size(); j++) {
						if (cvalue.equals(Cond[i].elementAt(j).toString())) {// 条件を比較
							point = point + Math.pow(10, CONDS_MAX-1-i) * (7-j);// 7～0ポイント
							break;
						}
					}
				}
			} else {
				point = point + Math.pow(10, CONDS_MAX-1-i) * 9;// 9ポイント
			}
		}
		return point;
	}

	// 条件に一致するかをチェックする
	public boolean checkCond(Element element) {
		boolean check = true;
		String value;// ワーク用
		for (int i=0; i<CONDS_MAX; i++) {
			if (Condkey[i]!=null && Condkey[i].length()>0 && element.hasAttribute(Condkey[i])) {// 条件がある？
				value = element.getAttribute(Condkey[i]);// Condkey[i]の属性を取得
				if (!value.equals(Cond[i].elementAt(choiceCond[i].getSelectedIndex()).toString())) {// 条件を比較
					check = false;
					break;
				}
			}
		}
		return check;
	}

	public void init(){
System.out.println("init");
		String filename = "";
		String value;
		value = getParameter("filename");//パラメータfilenameの値を受け取る
		if (value != null) {
			filename = value;
		}
//		value = getParameter("mapbasey");//パラメータmapbaseyの値を受け取る
//		if (value != null) {
//			MAPY1 = Integer.parseInt(mapbasey);
//		}
		value = getParameter("pisize");//パラメータpisizeの値を受け取る
		if (value != null) {
			pisize = Integer.parseInt(value);
		}
		value = getParameter("misize");//パラメータmisizeの値を受け取る
		if (value != null) {
			misize = Integer.parseInt(value);
		}
		value = getParameter("colayout");//パラメータcolayoutの値を受け取る
		if (value != null) {
			colayout = Integer.parseInt(value);
		}
		value = getParameter("disptype");//パラメータdisptypeの値を受け取る
		if (value != null) {
			disptype = Integer.parseInt(value);
		}
		value = getParameter("scenariofile");//パラメータscenariofileの値を受け取る
		if (value != null) {
			scenariofile = value;
		} else {
			scenariofile = "";
		}
		value = getParameter("scenariono");//パラメータscenarionoの値を受け取る
		if (value != null) {
			scenariono = Integer.parseInt(value);
		} else {
			scenariono = -1;
		}
		value = getParameter("initid");//パラメータinitidの値を受け取る
		if (value != null) {
			initidparam = value;
		}
		value = getParameter("initmuki");//パラメータinitmukiの値を受け取る
		if (value != null) {
			initmukiparam = value;
		}

		mt = new MediaTracker(this);
		initComponents();
		initSize();
		initLayout();
		ReadInitdata();
		int index = getMapxmlIndex(filename);
		if (index >= 0) {
			choiceMapxml.select(index);
		}
		tfMapxml.setText(choiceMapxml.getSelectedItem());

		ReadData();

		if (scenariofile.length() > 0) {
			ReadScenarioXMLFile(scenariofile);
		}

//		show();
	}

//	public void start(){
//System.out.println("start");
//	}

//	public void stop(){
//System.out.println("stop");
//	}

//	public void destroy(){
//System.out.println("destroy");
//	}

	private void initComponents() {
		setLayout(null);
//		setTitle("FREE WALKER");
		setBackground(bcolor);

		// メイン設定パネル
		add(pnMain);
		pnMain.setBackground(pbcolor);
		pnMain.setLayout(null);

		// 地図
		pnMain.add(choiceMapxml);
		Mapxml = new Vector();
		pnMain.add(btn1);
		btn1.addActionListener(this);
//		btn1.setEnabled(false);
		pnMain.add(btn2);
		btn2.addActionListener(this);
		pnMain.add(tfMapxml);
		tfMapxml.setEditable(false);
		tfMapxml.setBackground(Color.white);

		// 条件
		for (int i=0; i<CONDS_MAX; i++) {
			choiceCond[i] = new Choice();
			pnMain.add(choiceCond[i]);
			choiceCond[i].setVisible(false);
		}

		// シナリオ
		pnMain.add(SRBtn);
		SRBtn.addActionListener(this);
		pnMain.add(choScena);
		choScena.addItemListener(this);
		pnMain.add(SSBtn);
		SSBtn.addActionListener(this);
		pnMain.add(SCBtn);
		SCBtn.addActionListener(this);
		pnMain.add(tfScena);
		tfScena.setEditable(false);
		tfScena.setBackground(Color.white);

		// パノラマ設定パネル
		add(pnPano);
		pnPano.setBackground(pbcolor);
		pnPano.setLayout(null);

		// パノラマ設定
		pnPano.add(choSpeed);
		choSpeed.addItem("速度：おそい");
		choSpeed.addItem("速度：ふつう");
		choSpeed.addItem("速度：はやい");
		choSpeed.select(1);
		choSpeed.addItemListener(this);
		pnPano.add(chkWaku);
		chkWaku.setForeground(pfcolor);
		chkWaku.setLabel("リンク枠");
		chkWaku.addItemListener(this);
		chkWaku.setState(true);

		// 地図設定パネル
		add(pnMap1);
		pnMap1.setBackground(pbcolor);
		pnMap1.setLayout(null);

		// 地図設定
		pnMap1.add(checkbox1);
		checkbox1.setForeground(pfcolor);
		checkbox1.setLabel("写真表示");
		checkbox1.addItemListener(this);
		checkbox1.addMouseListener(new MyMouseAdapter());
		checkbox1.setState(true);
		pnMap1.add(checkbox2);
		checkbox2.setForeground(pfcolor);
		checkbox2.setLabel("中心固定");
		checkbox2.addItemListener(this);
		checkbox2.addMouseListener(new MyMouseAdapter());
		pnMap1.add(checkbox3);
		checkbox3.setForeground(pfcolor);
		checkbox3.setLabel("上固定");
		checkbox3.addItemListener(this);
		checkbox3.addMouseListener(new MyMouseAdapter());
		pnMap1.add(checkbox4);
		checkbox4.setForeground(pfcolor);
		checkbox4.setLabel("ふきだし");
		checkbox4.addItemListener(this);
		checkbox4.addMouseListener(new MyMouseAdapter());
		pnMap1.add(checkbox5);
		checkbox5.setForeground(pfcolor);
		checkbox5.setLabel("シナリオ");
		checkbox5.addItemListener(this);
		checkbox5.addMouseListener(new MyMouseAdapter());
		pnMap1.add(checkbox6);
		checkbox6.setForeground(pfcolor);
		checkbox6.setLabel("方角表示");
		checkbox6.addItemListener(this);
		checkbox6.addMouseListener(new MyMouseAdapter());
		checkbox6.setState(true);
		pnMap1.add(checkbox7);
		checkbox7.setForeground(pfcolor);
		checkbox7.setLabel("経路表示");
		checkbox7.addItemListener(this);
		checkbox7.addMouseListener(new MyMouseAdapter());

		// ダミーパネル
		add(pnDum1);
		pnDum1.setBackground(pbcolor);
		pnDum1.setLayout(null);

		// ラベル
		add(label1);
		label1.setForeground(fcolor);
		add(label2);
		label2.setForeground(fcolor);

		// シナリオ用
		add(textArea1);
		textArea1.setVisible(false);
//		textArea1.setEditable(false);
		textArea1.setBackground(Color.black);
		textArea1.setForeground(Color.white);
		add(textArea2);
		textArea2.setVisible(false);
//		textArea2.setEditable(false);
		textArea2.setBackground(Color.black);
		textArea2.setForeground(Color.white);

		add(canvas1);
		canvas1.addMouseListener(new MyMouseAdapter());
		canvas1.addMouseMotionListener(new MyMouseMotionAdapter());

		// 画像を読み込む
	try {
		img_arwicon = getImage(getCodeBase(), "arwicon.gif");
		img_mapicon = getImage(getCodeBase(), "mapicon2.gif");
		img_guide = getImage(getCodeBase(), "guide.gif");
		img_logo = getImage(getCodeBase(), "logo.gif");
	} catch (Exception e) {
		e.printStackTrace();
	}

		cvLogo = new Canvas() {
			public void paint(Graphics g) {
				g.drawImage(img_logo,0,0,getSize().width,getSize().height,this);
			}
		};
		add(cvLogo);

		tm1 = new Timer(true);
		tm2 = new Timer(true);
//		addWindowListener(new WinAdapter());
	}

	// 以下の値を設定
	//	static int FRAMEW = 760;			// フレーム幅
	//	static int FRAMEH = 640;			// フレーム高さ
	//	static int CANVASX = 0;				// キャンバスＸ
	//	static int CANVASY = 25;			// キャンバスＹ
	//	static int CANVASW = 760;			// キャンバス幅
	//	static int CANVASH = 580;			// キャンバス高さ
	//	static int PANOY1 = 20;				// パノラマ表示基準位置ｙ
	//	static int PANOW = 480;				// パノラマ表示幅
	//	static int PANOH = 240;				// パノラマ表示高さ
	//	static int MAPY1 = 320;				// 地図表示基準位置ｙ
	//	static int MAPW = 240;				// 地図表示幅
	//	static int MAPH = 240;				// 地図表示高さ
	//	static int FUKIW = 160;				// ふきだし表示幅
	//	static int FUKIH = 240;				// ふきだし表示高さ
	//	static int FUKIY1 = 320;			// ふきだし表示ｙ
	//	static int MMX = CANVASW/2;			// 地図の中心ｘ
	//	static int MMY = MAPY1+MAPH/2;		// 地図の中心ｙ
	//	static int MX1 = MMX-MAPW/2;		// 地図の左位置
	//	static int MX2 = MMX+MAPW/2;		// 地図の右位置
	//	static int PMX = CANVASW/2;			// パノラマの中心ｘ
	//	static int PMY = PANOY1+PANOH/2;	// パノラマの中心ｙ
	//	static int PX1 = PMX-PANOW/2;		// パノラマの左位置
	//	static int PX2 = PMX+PANOW/2;		// パノラマの右位置
	private void initSize() {
		if (pisize < 240 || pisize > 320) return;
		if (misize < 240 || misize > 320) return;
		FUKIW = misize*2/3;// ふきだし表示幅
		FUKIH = misize;// ふきだし表示高さ
//		CANVASW = Math.max(pisize*2 + MIS*8 + ARWW1*2 + ARWW2*2, misize + FUKIW*2 + MIS*8);
		CANVASW = 856;
		CANVASH = Math.max(610, MIS*2 + pisize + BTNY + MIS*4 + misize + ICONSIZE + MIS);
		if (colayout == 0) {// ボタン上レイアウト
			CANVASX = SPACEX;
			CANVASY = SPACEY + 78;
			FRAMEH = CANVASY + CANVASH + 5;
			FRAMEW = CANVASX + CANVASW + 5;
		} else if (colayout == 1) {// ボタン下レイアウト
			CANVASX = SPACEX;
			CANVASY = SPACEY;
			FRAMEH = CANVASY + CANVASH + 78 + 5;
			FRAMEW = CANVASX + CANVASW + 5;
		} else if (colayout == 2) {// ボタン左レイアウト
			CANVASX = SPACEX + 140;
			CANVASY = SPACEY;
			FRAMEH = CANVASY + CANVASH + 5;
			FRAMEW = CANVASX + CANVASW + 5;
		} else {// ボタン右レイアウト
			CANVASX = SPACEX;
			CANVASY = SPACEY;
			FRAMEH = CANVASY + CANVASH + 5;
			FRAMEW = CANVASX + CANVASW + 140 + 5;
		}
		if (disptype == 2) {// 地図レイアウト
			PANOY1 = 0;
			PANOW = 0;
			PANOH = 0;
			MAPY1 = MIS*2;
			MAPW = pisize + MIS*2 + misize;
			MAPH = pisize + MIS*2 + misize;
			MMX = CANVASW/2+(FUKIW+MIS)/2;// 地図の中心ｘ
			MMY = MAPY1+MAPH/2;// 地図の中心ｙ
			MX1 = MMX-MAPW/2;// 地図の左位置
			MX2 = MMX+MAPW/2;// 地図の右位置
			PMX = 0;// パノラマの中心ｘ
			PMY = 0;// パノラマの中心ｙ
			PX1 = 0;// パノラマの左位置
			PX2 = 0;// パノラマの右位置
			FUKIW = Math.min(FUKIW, MX1-MIS*3);// ふきだし表示幅
		} else if (disptype == 3) {// 道案内レイアウト
			PANOY1 = MIS*2;
			PANOW = pisize*2;
			PANOH = pisize;
			MAPY1 = PANOY1 + PANOH + BTNY + MIS*4;
			MAPW = misize;
			MAPH = misize;
			MMX = CANVASW/2+MAPW/3+MAPW/2;// 地図の中心ｘ
			MMY = MAPY1+MAPH/2;// 地図の中心ｙ
			MX1 = MMX-MAPW/2;// 地図の左位置
			MX2 = MMX+MAPW/2;// 地図の右位置
			PMX = CANVASW/2;// パノラマの中心ｘ
			PMY = PANOY1+PANOH/2;// パノラマの中心ｙ
			PX1 = PMX-PANOW/2;// パノラマの左位置
			PX2 = PMX+PANOW/2;// パノラマの右位置
		} else {// 標準レイアウト
			PANOY1 = MIS*2;
			PANOW = pisize*2;
			PANOH = pisize;
			MAPY1 = PANOY1 + PANOH + BTNY + MIS*4;
			MAPW = misize;
			MAPH = misize;
			MMX = CANVASW/2;// 地図の中心ｘ
			MMY = MAPY1+MAPH/2;// 地図の中心ｙ
			MX1 = MMX-MAPW/2;// 地図の左位置
			MX2 = MMX+MAPW/2;// 地図の右位置
			PMX = CANVASW/2;// パノラマの中心ｘ
			PMY = PANOY1+PANOH/2;// パノラマの中心ｙ
			PX1 = PMX-PANOW/2;// パノラマの左位置
			PX2 = PMX+PANOW/2;// パノラマの右位置
		}
		FUKIY1 = MAPY1 + MAPH - FUKIH;// ふきだし表示ｙ
	}

	private void initLayout() {
		setBounds(0,0,FRAMEW,FRAMEH);
		label1.setBounds(CANVASX+PX2+ICONSIZE,CANVASY+PANOY1,100,22);
		label2.setBounds(CANVASX+PX1,CANVASY+PANOY1+PANOH+ICONSIZE,PANOW,22);
		textArea1.setBounds(CANVASX+MX1-MIS*2-FUKIW+MIS,CANVASY+FUKIY1+FUKIW,FUKIW-MIS*2,FUKIH-FUKIW-MIS);
		textArea2.setBounds(CANVASX+MX2+MIS*3,CANVASY+FUKIY1+FUKIW,FUKIW-MIS*2,FUKIH-FUKIW-MIS);
		canvas1.setBounds(CANVASX,CANVASY,CANVASW,CANVASH);
		if (disptype == 0) {// 標準レイアウト
			tfMapxml.setVisible(false);
			choiceMapxml.setVisible(true);
			btn1.setVisible(true);
			btn2.setVisible(true);
			for (int i = 0; i < CONDS_MAX; i++) {// CONDS_MAX数分ループ
				if (choiceCond[i].getItemCount() > 0) {
					choiceCond[i].setVisible(true);
				}
			}
			SRBtn.setVisible(true);
			SSBtn.setVisible(true);
			SCBtn.setVisible(true);
			SSBTNLABEL1 = "シナリオ開始";
			SCBTNLABEL3 = "シナリオ中止";
			SSBtn.setLabel(SSBTNLABEL1);
			tfScena.setVisible(false);
			choScena.setVisible(true);
			choSpeed.setVisible(true);
			checkbox2.setVisible(true);
			checkbox2.setState(false);
			map_centermode = 0;
			checkbox3.setVisible(true);
			checkbox3.setState(false);
			map_upmode = 0;
			checkbox4.setVisible(false);
			checkbox4.setState(false);
			map_popmode = 0;
			checkbox5.setVisible(true);
			checkbox5.setState(false);
			map_scemode = 0;
			checkbox6.setVisible(true);
			checkbox7.setVisible(true);
			chkWaku.setVisible(true);
			if (colayout == 0 || colayout == 1) {// ボタン上または下レイアウト
				choiceMapxml.setBounds(3,3,130,22);
				btn1.setBounds(133,3,60,22);
				btn2.setBounds(193,3,60,22);
				for (int i=0; i<CONDS_MAX; i++) {
					choiceCond[i].setBounds(3+90*i,28,90,22);
				}
				SRBtn.setBounds(3,53,85,22);
				choScena.setBounds(88,53,130,22);
				SSBtn.setBounds(218,53,85,22);
				SCBtn.setBounds(303,53,60,22);
				choSpeed.setBounds(3,28,95,22);
				chkWaku.setBounds(3,53,80,22);
				checkbox1.setBounds(147,3,72,22);
				checkbox2.setBounds(3,28,72,22);
				checkbox3.setBounds(75,28,72,22);
				checkbox4.setBounds(147,28,72,22);
				checkbox5.setBounds(3,53,72,22);
				checkbox6.setBounds(75,53,72,22);
				checkbox7.setBounds(147,53,72,22);
			} else {// ボタン左または右レイアウト
				choiceMapxml.setBounds(5,3,130,22);
				btn1.setBounds(5,28,63,22);
				btn2.setBounds(73,28,62,22);
				for (int i=0; i<CONDS_MAX; i++) {
					choiceCond[i].setBounds(5,53+25*i,90,22);
				}
				SRBtn.setBounds(5,153,85,22);
				choScena.setBounds(5,178,130,22);
				SSBtn.setBounds(5,203,130,22);
				SCBtn.setBounds(5,228,130,22);
				choSpeed.setBounds(5,3,95,22);
				chkWaku.setBounds(5,28,80,22);
				checkbox1.setBounds(5,3,72,22);
				checkbox2.setBounds(5,28,72,22);
				checkbox3.setBounds(5,53,72,22);
				checkbox4.setBounds(5,78,72,22);
				checkbox5.setBounds(5,103,72,22);
				checkbox6.setBounds(5,128,72,22);
				checkbox7.setBounds(5,153,72,22);
			}
			if (colayout == 0) {// ボタン上レイアウト
				panelLayoutTB(SPACEY, 78, 140, 366, 101, 220);// パネルレイアウト設定
			} else if (colayout == 1) {// ボタン下レイアウト
				panelLayoutTB(CANVASY+CANVASH, 78, 140, 366, 101, 220);// パネルレイアウト設定
			} else if (colayout == 2) {// ボタン左レイアウト
				panelLayoutLR(SPACEX, 140, 92, 253, 78, 178);// パネルレイアウト設定
			} else {// ボタン右レイアウト
				panelLayoutLR(CANVASX+CANVASW, 140, 92, 253, 78, 178);// パネルレイアウト設定
			}
		} else if (disptype == 1) {// ＨＰレイアウト
			tfMapxml.setVisible(false);
			choiceMapxml.setVisible(true);
			btn1.setVisible(true);
			btn2.setVisible(true);
			for (int i = 0; i < CONDS_MAX; i++) {// CONDS_MAX数分ループ
				if (choiceCond[i].getItemCount() > 0) {
					choiceCond[i].setVisible(true);
				}
			}
			SRBtn.setVisible(false);
			SSBtn.setVisible(true);
			SCBtn.setVisible(true);
			SSBTNLABEL1 = "移動";
			SCBTNLABEL3 = "移動中止";
			SSBtn.setLabel(SSBTNLABEL1);
			tfScena.setVisible(false);
			choScena.setVisible(true);
			choSpeed.setVisible(true);
			checkbox2.setVisible(true);
			checkbox2.setState(false);
			map_centermode = 0;
			checkbox3.setVisible(true);
			checkbox3.setState(false);
			map_upmode = 0;
			checkbox4.setVisible(false);
			checkbox4.setState(false);
			map_popmode = 0;
			checkbox5.setVisible(false);
			checkbox5.setState(false);
			map_scemode = 0;
			checkbox6.setVisible(false);
			checkbox7.setVisible(false);
			chkWaku.setVisible(false);
			if (colayout == 0 || colayout == 1) {// ボタン上または下レイアウト
				choiceMapxml.setBounds(3,3,130,22);
				btn1.setBounds(133,3,60,22);
				btn2.setBounds(193,3,60,22);
				for (int i=0; i<CONDS_MAX; i++) {
					choiceCond[i].setBounds(3+85*i,28,85,22);
				}
				choScena.setBounds(88,53,130,22);
				SSBtn.setBounds(218,53,85,22);
				SCBtn.setBounds(303,53,60,22);
				choSpeed.setBounds(3,28,95,22);
				checkbox1.setBounds(147,3,72,22);
				checkbox2.setBounds(3,28,72,22);
				checkbox3.setBounds(75,28,72,22);
				checkbox4.setBounds(147,28,72,22);
			} else {// ボタン左または右レイアウト
				choiceMapxml.setBounds(5,3,130,22);
				btn1.setBounds(5,28,63,22);
				btn2.setBounds(73,28,62,22);
				for (int i=0; i<CONDS_MAX; i++) {
					choiceCond[i].setBounds(5,53+25*i,85,22);
				}
				choScena.setBounds(5,153,130,22);
				SSBtn.setBounds(5,178,130,42);
				SCBtn.setBounds(5,223,130,27);
				choSpeed.setBounds(5,3,95,22);
				checkbox1.setBounds(5,3,72,22);
				checkbox2.setBounds(5,28,72,22);
				checkbox3.setBounds(5,53,72,22);
				checkbox4.setBounds(5,78,72,22);
			}
			if (colayout == 0) {// ボタン上レイアウト
				panelLayoutTB(SPACEY, 78, 140, 366, 101, 220);// パネルレイアウト設定
			} else if (colayout == 1) {// ボタン下レイアウト
				panelLayoutTB(CANVASY+CANVASH, 78, 140, 366, 101, 220);// パネルレイアウト設定
			} else if (colayout == 2) {// ボタン左レイアウト
				panelLayoutLR(SPACEX, 140, 92, 253, 78, 178);// パネルレイアウト設定
			} else {// ボタン右レイアウト
				panelLayoutLR(CANVASX+CANVASW, 140, 92, 253, 78, 178);// パネルレイアウト設定
			}
		} else if (disptype == 2) {// 地図レイアウト
			tfMapxml.setVisible(true);
			choiceMapxml.setVisible(false);
			btn1.setVisible(false);
			btn2.setVisible(false);
			for (int i = 0; i < CONDS_MAX; i++) {// CONDS_MAX数分ループ
				if (choiceCond[i].getItemCount() > 0) {
					choiceCond[i].setVisible(false);
				}
			}
			SRBtn.setVisible(false);
			if (scenariono >= 0) {
				SSBtn.setVisible(true);
				SCBtn.setVisible(true);
			} else {
				SSBtn.setVisible(false);
				SCBtn.setVisible(false);
			}
			SSBTNLABEL1 = "道案内－開始";
			SCBTNLABEL3 = "道案内－中止";
			SSBtn.setLabel(SSBTNLABEL1);
			if (scenariono >= 0) {
				tfScena.setVisible(true);
			} else {
				tfScena.setVisible(false);
			}
			choScena.setVisible(false);
			choSpeed.setVisible(false);
			checkbox2.setVisible(false);
			checkbox2.setState(false);
			map_centermode = 0;
			checkbox3.setVisible(false);
			checkbox3.setState(false);
			map_upmode = 0;
			checkbox4.setVisible(false);
			checkbox4.setState(false);
			map_popmode = 0;
			checkbox5.setVisible(false);
			if (scenariono >= 0) {
				checkbox5.setState(true);
				map_scemode = 1;
			} else {
				checkbox5.setState(false);
				map_scemode = 0;
			}
			checkbox6.setVisible(false);
			checkbox7.setVisible(false);
			chkWaku.setVisible(false);
			if (colayout == 0 || colayout == 1) {// ボタン上または下レイアウト
				tfMapxml.setBounds(5,5,130,20);
				tfScena.setBounds(5,30,130,20);
				SSBtn.setBounds(140,5,130,44);
				SCBtn.setBounds(275,5,80,44);
				checkbox1.setBounds(147,3,72,22);
				checkbox5.setBounds(3,53,72,22);
			} else {// ボタン左または右レイアウト
				tfMapxml.setBounds(5,5,130,20);
				tfScena.setBounds(5,30,130,20);
				SSBtn.setBounds(5,55,130,55);
				SCBtn.setBounds(5,115,130,33);
				checkbox1.setBounds(5,5,72,22);
				checkbox5.setBounds(5,28,72,22);
			}
			if (colayout == 0) {// ボタン上レイアウト
				panelLayoutTB(SPACEY, 78, 140, 360, 101, 220);// パネルレイアウト設定
			} else if (colayout == 1) {// ボタン下レイアウト
				panelLayoutTB(CANVASY+CANVASH, 78, 140, 360, 101, 220);// パネルレイアウト設定
			} else if (colayout == 2) {// ボタン左レイアウト
				panelLayoutLR(SPACEX, 140, 92, 153, 54, 54);// パネルレイアウト設定
			} else {// ボタン右レイアウト
				panelLayoutLR(CANVASX+CANVASW, 140, 92, 153, 54, 54);// パネルレイアウト設定
			}
		} else if (disptype == 3) {// 道案内レイアウト
			tfMapxml.setVisible(true);
			choiceMapxml.setVisible(false);
			btn1.setVisible(false);
			btn2.setVisible(false);
			for (int i = 0; i < CONDS_MAX; i++) {// CONDS_MAX数分ループ
				if (choiceCond[i].getItemCount() > 0) {
					choiceCond[i].setVisible(false);
				}
			}
			SRBtn.setVisible(false);
			SSBtn.setVisible(true);
			SCBtn.setVisible(true);
			SSBTNLABEL1 = "道案内－開始";
			SCBTNLABEL3 = "道案内－中止";
			SSBtn.setLabel(SSBTNLABEL1);
			tfScena.setVisible(true);
			choScena.setVisible(false);
			choSpeed.setVisible(true);
			checkbox2.setVisible(false);
			checkbox2.setState(false);
			map_centermode = 0;
			checkbox3.setVisible(false);
			checkbox3.setState(false);
			map_upmode = 0;
			checkbox4.setVisible(false);
			checkbox4.setState(false);
			map_popmode = 0;
			checkbox5.setVisible(false);
			checkbox5.setState(true);
			map_scemode = 1;
			checkbox6.setVisible(false);
			checkbox7.setVisible(false);
			chkWaku.setVisible(false);
			if (colayout == 0 || colayout == 1) {// ボタン上または下レイアウト
				tfMapxml.setBounds(5,5,130,20);
				tfScena.setBounds(5,30,130,20);
				SSBtn.setBounds(140,5,130,44);
				SCBtn.setBounds(275,5,80,44);
				choSpeed.setBounds(3,28,95,22);
				checkbox1.setBounds(147,3,72,22);
			} else {// ボタン左または右レイアウト
				tfMapxml.setBounds(5,5,130,20);
				tfScena.setBounds(5,30,130,20);
				SSBtn.setBounds(5,55,130,55);
				SCBtn.setBounds(5,115,130,33);
				choSpeed.setBounds(5,5,95,22);
				checkbox1.setBounds(5,5,72,22);
			}
			if (colayout == 0) {// ボタン上レイアウト
				panelLayoutTB(SPACEY, 78, 140, 360, 101, 220);// パネルレイアウト設定
			} else if (colayout == 1) {// ボタン下レイアウト
				panelLayoutTB(CANVASY+CANVASH, 78, 140, 360, 101, 220);// パネルレイアウト設定
			} else if (colayout == 2) {// ボタン左レイアウト
				panelLayoutLR(SPACEX, 140, 92, 153, 54, 54);// パネルレイアウト設定
			} else {// ボタン右レイアウト
				panelLayoutLR(CANVASX+CANVASW, 140, 92, 153, 54, 54);// パネルレイアウト設定
			}
		}
	}

	// パネルレイアウト設定
	private void panelLayoutTB(int y, int h, int w1, int w2, int w3, int w4) {
		int x1 = SPACEX;
		int x2 = x1+w1+3;
		int x3 = x2+w2+3;
		int x4 = x3+w3+3;
		int x5 = x4+w4+3;
		int w5 = Math.max(SPACEX+CANVASW-x5, 0);
		cvLogo.setBounds(x1,y,w1,h);
		pnMain.setBounds(x2,y,w2,h);
		pnPano.setBounds(x3,y,w3,h);
		pnMap1.setBounds(x4,y,w4,h);
		pnDum1.setBounds(x5,y,w5,h);
	}

	// パネルレイアウト設定
	private void panelLayoutLR(int x, int w, int h1, int h2, int h3, int h4) {
		int y1 = SPACEY;
		int y2 = y1+h1+3;
		int y3 = y2+h2+3;
		int y4 = y3+h3+3;
		int y5 = y4+h4+3;
		int h5 = Math.max(SPACEY+CANVASH-y5, 0);
		cvLogo.setBounds(x,y1,w,h1);
		pnMain.setBounds(x,y2,w,h2);
		pnPano.setBounds(x,y3,w,h3);
		pnMap1.setBounds(x,y4,w,h4);
		pnDum1.setBounds(x,y5,w,h5);
	}

	class MyMouseMotionAdapter extends MouseMotionAdapter {
		public void mouseMoved(MouseEvent event) {
			Object object = event.getSource();
			if (object == canvas1)
				canvas1_MouseMoved(event);
		}
		public void mouseDragged(MouseEvent event) {
			Object object = event.getSource();
			if (object == canvas1)
				canvas1_MouseDragged(event);
		}
	}
	class MyMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent event) {
			Object object = event.getSource();
			if (object == canvas1)
				canvas1_MousePressed(event);
		}
		public void mouseReleased(MouseEvent event) {
			Object object = event.getSource();
			if (object == canvas1)
				canvas1_MouseReleased(event);
		}
		public void mouseClicked(MouseEvent event) {
			Object object = event.getSource();
			if (object == canvas1)
				canvas1_MouseClicked(event);
		}
		public void mouseEntered(MouseEvent event) {
			Object object = event.getSource();
			if (object == checkbox1) {
				label2.setText("ＰＯＩを写真で表示します。");
			} else if (object == checkbox2) {
				label2.setText("現在地を常に地図の中心にします。");
			} else if (object == checkbox3) {
				label2.setText("現在地の向きを常に上向きにします。");
			} else if (object == checkbox4) {
				label2.setText("進行方向にＰＯＩがある場合、ふきだしを表示します。");
			} else if (object == checkbox5) {
				label2.setText("シナリオルートを表示します。");
			} else if (object == checkbox6) {
				label2.setText("方角表示を表示します。");
			} else if (object == checkbox7) {
				label2.setText("全ての経路を表示します。");
			}
		}
		public void mouseExited(MouseEvent event) {
			Object object = event.getSource();
			if (object == checkbox1) {
				label2.setText("");
			} else if (object == checkbox2) {
				label2.setText("");
			} else if (object == checkbox3) {
				label2.setText("");
			} else if (object == checkbox4) {
				label2.setText("");
			} else if (object == checkbox5) {
				label2.setText("");
			} else if (object == checkbox6) {
				label2.setText("");
			} else if (object == checkbox7) {
				label2.setText("");
			}
		}
	}

	void canvas1_MouseMoved(MouseEvent event) {
	try {
		boolean repaintflg = false;
		int newno = -1;
		int newcur = 0;
		int newwakuno = -1;
		int newbuttonno = -1;
		int evx = event.getX();
		int evy = event.getY();
		int drx = MAPW/2;
		int dry = MAPH/2;
		int ddx, ddy;
		if (evx>MX1 && evx<MX2 && evy>MAPY1 && evy<MAPY1+MAPH) {// 地図内にマウスが入った？
			for (int i=0; i<Fpoi.length; i++) {
				ddx = Calc_ddx(Fpoi[i].mx, Fpoi[i].my);// 表示中心からの相対位置
				ddy = Calc_ddy(Fpoi[i].mx, Fpoi[i].my);// 表示中心からの相対位置
				if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// ふきだしＰＯＩが地図表示範囲内にある？
					if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// ふきだしＰＯＩにマウスが入った？
						newno = i;
					}
				}
			}
			if (disptype != 2 && (pata_mode == 0 || pata_mode == 4 || pata_mode == 5 || pata_mode == 6)) {// 地図レイアウト以外かつ、停止中または一時停止中
				for (int i=0; i<Pano.length; i++) {
					if (i == point) continue;
					ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
					ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// パノラマポイントが地図表示範囲内にある？
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// パノラマポイントにマウスが入った？
							newcur = 1;
						}
					}
				}
			}
			if (disptype == 2 && map_scemode == 0) {// 地図レイアウトかつ、シナリオ非表示
				for (int i=0; i<Pano.length; i++) {
					ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
					ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// パノラマポイントが地図表示範囲内にある？
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// パノラマポイントにマウスが入った？
							newcur = 1;
						}
					}
				}
			}
		}
		if (pata_mode != 2) {// パノラマ画像表示中
			if (evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// パノラマ画像内にマウスが入った？
				int pw = Pano[point].img.getWidth(this);
				int ph = Pano[point].img.getHeight(this);
//				int py1 = PANOY1+(PANOH-ph)/2;
//				int c = linkCheck(imgX(pw,evx), evy-py1);
				int c = linkCheck(imgX(pw,evx), imgY(ph,evy));
				if (c >= 0) {
					LinkArea la = links[point].linkSet.area[c];
					if (task1 == null && pata_mode != 3 && la.id >= 0 && la.id < Pano.length) {// パタパタ開始
						newwakuno = c;
						newcur = 1;
					} else if (la.id >= LINKIDMIN && la.id <= WARPIDMIN-1) {// リンク
						newwakuno = c;
						newcur = 1;
					} else if (task1 == null && pata_mode != 3 && la.id >= WARPIDMIN) {// ワープ
						newwakuno = c;
						newcur = 1;
					}
				}
			}
			if (evx>PX1-10-ARWW1-10-ARWW2 && evx<PX1-10-ARWW1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {// 左矢印にマウスが入った？
				newbuttonno = 1;
				newcur = 1;
			}
			if (evx>PX1-10-ARWW1 && evx<PX1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {// 左矢印にマウスが入った？
				newbuttonno = 2;
				newcur = 1;
			}
			if (evx>PX2+10 && evx<PX2+10+ARWW1 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {// 右矢印にマウスが入った？
				newbuttonno = 3;
				newcur = 1;
			}
			if (evx>PX2+10+ARWW1+10 && evx<PX2+10+ARWW1+10+ARWW2 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {// 右矢印にマウスが入った？
				newbuttonno = 4;
				newcur = 1;
			}
			if (evx>PX1-ICONSIZE && evx<PX1 && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {// 縮小にマウスが入った？
				newbuttonno = 5;
				newcur = 1;
			}
			if (evx>PX2 && evx<PX2+ICONSIZE && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {// 拡大にマウスが入った？
				newbuttonno = 6;
				newcur = 1;
			}
			if (map_centermode == 0) {// 中心固定でない
				if (evx>MX1-ICONSIZE && evx<MX1 && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//地図左押下
					newbuttonno = 7;
					newcur = 1;
				}
				if (evx>MX2 && evx<MX2+ICONSIZE && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//地図右押下
					newbuttonno = 8;
					newcur = 1;
				}
				if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図上押下
					newbuttonno = 9;
					newcur = 1;
				}
				if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図下押下
					newbuttonno = 10;
					newcur = 1;
				}
			}
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図左下押下（縮小）
				newbuttonno = 11;
				newcur = 1;
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図右下押下（拡大）
				newbuttonno = 12;
				newcur = 1;
			}
			if (map_upmode == 0) {// 上固定でない
				if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図左上押下（左回転）
					newbuttonno = 13;
					newcur = 1;
				}
				if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図右上押下（右回転）
					newbuttonno = 14;
					newcur = 1;
				}
			}
		}
		if (fpoi_mode == 1) {// ふきだし固定時にはマウスが外れてもクリアしないように
			if (newno >= 0 && fpoi_no != newno) {
				fpoi_no = newno;
				repaintflg = true;
			}
		} else {
			if (fpoi_no != newno) {
				fpoi_no = newno;
				repaintflg = true;
			}
		}
		if (cursor != newcur) {
			cursor = newcur;
			if (cursor == 1) {
				canvas1.setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else {
				canvas1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		if (waku_no != newwakuno) {
			waku_no = newwakuno;
			repaintflg = true;
		}
		if (button_no != newbuttonno) {
			button_no = newbuttonno;
			repaintflg = true;
		}
		if (repaintflg) {
			canvas1.repaint();
		}
	} catch (java.lang.Exception e) {
		System.out.println("Error!! (MouseMoved)");
	}
	}

	void canvas1_MousePressed(MouseEvent event) {
	try {
		int evx = event.getX();
		int evy = event.getY();
//		mapw = img_map.getWidth(this);
//		maph = img_map.getHeight(this);
		pano_w = Pano[point].img.getWidth(this);
		if (evx>PX1-10-ARWW1 && evx<PX1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//左矢印押下
			rota_mode = 1;
			d_muki = -PANO_ROTA_UNIT;
			tm2start();// タイマースタート
			canvas1.repaint();
		}
		if (evx>PX2+10 && evx<PX2+10+ARWW1 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//右矢印押下
			rota_mode = 2;
			d_muki = PANO_ROTA_UNIT;
			tm2start();// タイマースタート
			canvas1.repaint();
		}
		if (evx>PX1-10-ARWW1-10-ARWW2 && evx<PX1-10-ARWW1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//左矢印押下
			rota_mode = 3;
			d_muki = -pano_w/12;
			tm2start();// タイマースタート
			canvas1.repaint();
		}
		if (evx>PX2+10+ARWW1+10 && evx<PX2+10+ARWW1+10+ARWW2 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//右矢印押下
			rota_mode = 4;
			d_muki = pano_w/12;
			tm2start();// タイマースタート
			canvas1.repaint();
		}
		if (map_centermode == 0) {// 中心固定でない
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//地図左押下
				map_mode = 1;
				d_mapx = (int)(Math.cos((-mapa+180)*Math.PI/180.0)*MAP_MOVE_UNIT);
				d_mapy = (int)(Math.sin((-mapa+180)*Math.PI/180.0)*MAP_MOVE_UNIT);
				tm2start();// タイマースタート
				canvas1.repaint();
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//地図右押下
				map_mode = 1;
				d_mapx = (int)(Math.cos(-mapa*Math.PI/180.0)*MAP_MOVE_UNIT);
				d_mapy = (int)(Math.sin(-mapa*Math.PI/180.0)*MAP_MOVE_UNIT);
				tm2start();// タイマースタート
				canvas1.repaint();
			}
			if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図上押下
				map_mode = 1;
				d_mapx = (int)(Math.cos((-mapa+270)*Math.PI/180.0)*MAP_MOVE_UNIT);
				d_mapy = (int)(Math.sin((-mapa+270)*Math.PI/180.0)*MAP_MOVE_UNIT);
				tm2start();// タイマースタート
				canvas1.repaint();
			}
			if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図下押下
				map_mode = 1;
				d_mapx = (int)(Math.cos((-mapa+90)*Math.PI/180.0)*MAP_MOVE_UNIT);
				d_mapy = (int)(Math.sin((-mapa+90)*Math.PI/180.0)*MAP_MOVE_UNIT);
				tm2start();// タイマースタート
				canvas1.repaint();
			}
		}
		if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図左下押下（縮小）
			if (maprate > MAP_ZOOM_MIN) {
				map_mode = 2;
				d_maprate = -MAP_ZOOM_UNIT;
				tm2start();// タイマースタート
				canvas1.repaint();
			}
		}
		if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図右下押下（拡大）
			if (maprate < MAP_ZOOM_MAX) {
				map_mode = 2;
				d_maprate = MAP_ZOOM_UNIT-1;
				tm2start();// タイマースタート
				canvas1.repaint();
			}
		}
		if (map_upmode == 0) {// 上固定でない
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図左上押下（左回転）
				map_mode = 3;
				d_mapa = 360-MAP_ROTA_UNIT;
				tm2start();// タイマースタート
				canvas1.repaint();
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図右上押下（右回転）
				map_mode = 3;
				d_mapa = MAP_ROTA_UNIT;
				tm2start();// タイマースタート
				canvas1.repaint();
			}
		}
		if (evx>PX1-ICONSIZE && evx<PX1 && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {//パノラマ縮小押下
			if (panorate > PANO_ZOOM_MIN) {
				map_mode = 4;
				d_panorate = -PANO_ZOOM_UNIT;
				tm2start();// タイマースタート
				canvas1.repaint();
			}
		}
		if (evx>PX2 && evx<PX2+ICONSIZE && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {//パノラマ拡大押下
			if (panorate < PANO_ZOOM_MAX) {
				map_mode = 4;
				d_panorate = PANO_ZOOM_UNIT-1;
				tm2start();// タイマースタート
				canvas1.repaint();
			}
		}
		if (evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// パノラマ画像内を押下
			rota_mode = 5;
			dragsx = evx;
		}
	} catch (java.lang.Exception e) {
		System.out.println("Error!! (MousePressed)");
	}
	}

	void canvas1_MouseReleased(MouseEvent event) {
	try {
		int evx = event.getX();
		int evy = event.getY();
		boolean repaintflg = false;
		if (rota_mode > 10) {// タイマーにより回転された
			rota_mode = 0;
			tm2stop();// タイマーストップ
			repaintflg = true;
		}
		if (rota_mode > 0) {// 回転中
			rota_mode = 0;
			tm2stop();// タイマーストップ
			if (evx>PX1-10-ARWW1 && evx<PX1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//左矢印クリック
				muki = (muki+pano_w-PANO_ROTA_UNIT) % pano_w;
				repaintflg = true;
			}
			if (evx>PX2+10 && evx<PX2+10+ARWW1 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//右矢印クリック
				muki = (muki+PANO_ROTA_UNIT) % pano_w;
				repaintflg = true;
			}
			if (evx>PX1-10-ARWW1-10-ARWW2 && evx<PX1-10-ARWW1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//左矢印クリック
				muki = (muki+pano_w-pano_w/12) % pano_w;
				repaintflg = true;
			}
			if (evx>PX2+10+ARWW1+10 && evx<PX2+10+ARWW1+10+ARWW2 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//右矢印クリック
				muki = (muki+pano_w/12) % pano_w;
				repaintflg = true;
			}
		}
		if (map_mode > 10) {// タイマーにより地図操作された
			map_mode = 0;
			tm2stop();// タイマーストップ
		}
		if (map_mode > 0) {// 地図操作中
			map_mode = 0;
			tm2stop();// タイマーストップ
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//地図左クリック
				int dx = (int)(Math.cos((-mapa+180)*Math.PI/180.0)*MAP_MOVE_UNIT);
				int dy = (int)(Math.sin((-mapa+180)*Math.PI/180.0)*MAP_MOVE_UNIT);
				mapx = mapx + dx;
				mapy = mapy + dy;
				fitMap2();
				repaintflg = true;
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//地図右クリック
				int dx = (int)(Math.cos(-mapa*Math.PI/180.0)*MAP_MOVE_UNIT);
				int dy = (int)(Math.sin(-mapa*Math.PI/180.0)*MAP_MOVE_UNIT);
				mapx = mapx + dx;
				mapy = mapy + dy;
				fitMap2();
				repaintflg = true;
			}
			if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図上クリック
				int dx = (int)(Math.cos((-mapa+270)*Math.PI/180.0)*MAP_MOVE_UNIT);
				int dy = (int)(Math.sin((-mapa+270)*Math.PI/180.0)*MAP_MOVE_UNIT);
				mapx = mapx + dx;
				mapy = mapy + dy;
				fitMap2();
				repaintflg = true;
			}
			if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図下クリック
				int dx = (int)(Math.cos((-mapa+90)*Math.PI/180.0)*MAP_MOVE_UNIT);
				int dy = (int)(Math.sin((-mapa+90)*Math.PI/180.0)*MAP_MOVE_UNIT);
				mapx = mapx + dx;
				mapy = mapy + dy;
				fitMap2();
				repaintflg = true;
			}
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図左下クリック（縮小）
				if (maprate > MAP_ZOOM_MIN) {
					maprate = maprate - (maprate+1)/MAP_ZOOM_UNIT;
					mpps = MPPS - Math.max(0, (100-maprate)/25);
					mois = MOIS - Math.max(0, (100-maprate)/5);
					fitMap2();
					repaintflg = true;
				}
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//地図右下クリック（拡大）
				if (maprate < MAP_ZOOM_MAX) {
					maprate = maprate + (maprate+1)/(MAP_ZOOM_UNIT-1);
					mpps = MPPS - Math.max(0, (100-maprate)/25);
					mois = MOIS - Math.max(0, (100-maprate)/5);
					repaintflg = true;
				}
			}
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図左上クリック（左回転）
				mapa = (mapa+360-MAP_ROTA_UNIT) % 360;
				repaintflg = true;
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1-ICONSIZE && evy<MAPY1) {//地図右上クリック（右回転）
				mapa = (mapa+MAP_ROTA_UNIT) % 360;
				repaintflg = true;
			}
			if (evx>PX1-ICONSIZE && evx<PX1 && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {//パノラマ縮小クリック
				if (panorate > PANO_ZOOM_MIN) {
					panorate = panorate - (panorate+1)/PANO_ZOOM_UNIT;
					repaintflg = true;
				}
			}
			if (evx>PX2 && evx<PX2+ICONSIZE && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {//パノラマ拡大クリック
				if (panorate < PANO_ZOOM_MAX) {
					panorate = panorate + (panorate+1)/(PANO_ZOOM_UNIT-1);
					repaintflg = true;
				}
			}
		}
		if (fpoi_mode == 1) {// ふきだし固定
			fpoi_mode = 0;// ふきだし固定クリア
			fpoi_no = -1;
			repaintflg = true;
		}
		int drx = MAPW/2;
		int dry = MAPH/2;
		int ddx, ddy;
		if (evx>MX1 && evx<MX2 && evy>MAPY1 && evy<MAPY1+MAPH) {// 地図内をクリック
			if (map_popmode == 0) {// ふきだしモード以外
				for (int i=0; i<Fpoi.length; i++) {
					ddx = Calc_ddx(Fpoi[i].mx, Fpoi[i].my);// 表示中心からの相対位置
					ddy = Calc_ddy(Fpoi[i].mx, Fpoi[i].my);// 表示中心からの相対位置
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// ふきだしＰＯＩが地図表示範囲内にある？
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// ふきだしＰＯＩをクリック
							fpoi_mode = 1;// ふきだし固定
							fpoi_no = i;
						}
					}
				}
			}
			if (disptype != 2 && (pata_mode == 0 || pata_mode == 4 || pata_mode == 5 || pata_mode == 6)) {// 地図レイアウト以外かつ、停止中または一時停止中
				for (int i=0; i<Pano.length; i++) {
					if (i == point) continue;
					ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
					ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// パノラマポイントが地図表示範囲内にある？
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// パノラマポイントをクリック
							int pw = pano_w;
							int pa = pano_a;
							PanoInit(i);
							muki = (int)(((muki*360+((double)pano_a-pa)*pw)*pano_w/360/pw) + pano_w) % pano_w;// 同じ向きにする
							repaintflg = true;
							break;
						}
					}
				}
			}
			if (disptype == 2 && map_scemode == 0) {// 地図レイアウトかつ、シナリオ非表示
				for (int i=0; i<Pano.length; i++) {
					ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
					ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// 表示中心からの相対位置
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// パノラマポイントが地図表示範囲内にある？
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// パノラマポイントをクリック
							int pw = pano_w;
							int pa = pano_a;
							PanoInit(i);
							muki = (int)(((muki*360+((double)pano_a-pa)*pw)*pano_w/360/pw) + pano_w) % pano_w;// 同じ向きにする
							maprate = 100;
							mpps = MPPS;
							mois = MOIS;
							mapx = Pano[point].mx;
							mapy = Pano[point].my;
							disptype = 1;// ＨＰレイアウト
							initSize();
							initLayout();
							fitMap1();
							repaintflg = true;
							break;
						}
					}
				}
			}
		} else if (pata_mode != 2 && evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// パノラマ画像内をクリック
			int ph = Pano[point].img.getHeight(this);
//			int py1 = PANOY1+(PANOH-ph)/2;
			int c = linkCheck(imgX(pano_w,evx), imgY(ph,evy));
			if (c >= 0) {
				LinkArea la = links[point].linkSet.area[c];
				if (task1 == null && pata_mode != 3 && la.id >= 0 && la.id < Pano.length) {// パタパタ開始
					tm1start();// タイマースタート
					pata_mode = 1;// パタパタ準備モード
					norepaint = 1;
					if (la.data2 > 0) {// パタパタが１枚以上ある
						pata_from = la.data1;
						pata_now = pata_from - 1;
						pata_to = pata_now + la.data2;
						if (pata_now < pata_to && PataImg[pata_now+1] == null) {// パタパタの先読みが必要か？
							PataImg[pata_now+1] = LoadImage(PataUri.elementAt(pata_now+1).toString());
						}
					}
					backpoint = point;
					backmuki = muki;
					PanoInit(la.id);
					pata_index = getAreaIndex(backpoint);// 領域インデックスを取得
					muki = getMuki(pata_index, 1);// 逆向きを取得
				} else if (la.id >= LINKIDMIN && la.id <= WARPIDMIN-1) {// リンク
					if (la.data2 == 1) {// テキスト表示
						int lo = la.data1;
						textPlayer = new TextPlayer(getCodeBase()+LinkObj[lo], 724);
					} else if (la.data2 == 2) {// 画像表示
						int lo = la.data1;
						picturePlayer = new PicturePlayer(LoadImage(LinkObj[lo]), 724);
					} else if (la.data2 == 3) {// 動画表示
						int lo = la.data1;
//						MovieProductPlayer mpp = new MovieProductPlayer(LinkObj[lo], 650);
					} else if (la.data2 == 4) {// ＨＴＭＬ表示
						int lo = la.data1;
						String htmlfile = LinkObj[lo];
						try {// Ｗｅｂ版
							if (htmlfile.startsWith("http:")) {
								getAppletContext().showDocument(new URL(htmlfile), "_blank");
							} else {
								getAppletContext().showDocument(new URL(getCodeBase()+htmlfile), "_blank");
							}
						} catch (MalformedURLException e) {  // エラー処理
							System.out.println("URL Error!");
							e.printStackTrace();
						}
					}
				} else if (task1 == null && pata_mode != 3 && la.id >= WARPIDMIN) {// ワープ
					int lo = la.data1;
					String data3 = la.data3;
					String data4 = la.data4;
					choiceMapxml.select(getMapxmlIndex(LinkObj[lo]));
					ReadData();
					if (!data3.equals("")) {// 初期ＩＤあり？
						int id = getId(data3);// ＩＤ番号を取得
						if (id >= 0) {
							PanoInit(id);
							backpoint = point;
						} else {
							System.out.println("初期ＩＤ:" + data3 + "が不明です");
						}
						if (!data4.equals("")) {// 初期向きあり？
							int gyaku = 0;
							if (data4.startsWith("!")) {// 逆向き？
								data4 = data4.substring(1);
								gyaku = 1;
							}
							int id2 = getId(data4);// ＩＤ番号を取得
							if (id2 < 0) {
								id2 = getId(data3 + ":" + data4);// ＩＤ番号を取得
							}
							if (id2 >= 0) {
								muki = getMuki(getAreaIndex(id2), gyaku);// 向きを取得する
							} else {
								System.out.println("初期向き:" + data4 + "が不明です");
							}
						}
					}
					repaintflg = true;
				}
			}
		} else if (pata_mode == 0 && evx>PX1 && evx<PX2 && evy>PANOY1+PANOH && evy<MAPY1-ICONSIZE) {//後ろクリック
			if (backpoint != point) {
				point = backpoint;
				muki = backmuki;
				repaintflg = true;
			}
		}
		if (repaintflg) {
			canvas1.repaint();
		}
	} catch (java.lang.Exception e) {
		System.out.println("Error!! (MouseReleased)");
		e.printStackTrace();
	}
	}

	void canvas1_MouseDragged(MouseEvent event) {
	try {
		int evx = event.getX();
		int evy = event.getY();
		long newperiod2 = DEF_PERIOD2;
		if (rota_mode == 5) {
			if (evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// パノラマ画像内をドラッグ
				if (dragsx > evx) {// 左へドラッグ
					rota_mode = 15;
					d_muki = -1;
					tm2start();// タイマースタート
				} else if (dragsx < evx) {// 右へドラッグ
					rota_mode = 15;
					d_muki = 1;
					tm2start();// タイマースタート
				}
			}
		} else if (rota_mode == 15) {
			if (evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// パノラマ画像内をドラッグ
				if (dragsx > evx) {// 左へドラッグ
					d_muki = -1;
					newperiod2 = Math.max(10, DEF_PERIOD2 - (dragsx-evx)/10*10);
					if (dragsx-evx > 50) d_muki = -2;
				} else if (dragsx < evx) {// 右へドラッグ
					d_muki = 1;
					newperiod2 = Math.max(10, DEF_PERIOD2 - (evx-dragsx)/10*10);
					if (evx-dragsx > 50) d_muki = 2;
				} else {
					d_muki = 0;
				}
			}
			if (newperiod2 != period2) {
				period2 = newperiod2;
				if (task2 != null) {
					tm2stop();// タイマーストップ
					tm2start(0, period2);// タイマースタート
				}
			}
		}
	} catch (java.lang.Exception e) {
		System.out.println("Error!! (MouseDragged)");
		e.printStackTrace();
	}
	}

	void canvas1_MouseClicked(MouseEvent event) {
	try {
	} catch (java.lang.Exception e) {
		System.out.println("Error!! (MouseClicked)");
	}
	}

	// 地図ＸＭＬインデックスを取得する
	int getMapxmlIndex(String str) {
		int index = Mapxml.size() - 1;
		for (; index >= 0; index--) {// Mapxml数分ループ
			if (str.equals(Mapxml.elementAt(index).toString())) {// インデックスが一致した？
				break;
			}
		}
		return index;
	}

	// ＩＤ番号を取得する
	int getId(String str) {
		int id = WarpId.size() - 1;
		for (; id >= 0; id--) {// WarpId数分ループ
			if (str.equals(WarpId.elementAt(id).toString())) {// ＩＤが一致した？
				return WARPIDMIN + id;
			}
		}
		id = LinkId.size() - 1;
		for (; id >= 0; id--) {// LinkId数分ループ
			if (str.equals(LinkId.elementAt(id).toString())) {// ＩＤが一致した？
				return LINKIDMIN + id;
			}
		}
		id = PanoId.size() - 1;
		for (; id >= 0; id--) {// PanoId数分ループ
			if (str.equals(PanoId.elementAt(id).toString())) {// ＩＤが一致した？
				break;
			}
		}
		return id;
	}

	// 領域インデックスを取得する
	int getAreaIndex(int toid) {
		int index = -1;
		for (int i = 0; i < links[point].linkSet.area.length; i++) {
			if (links[point].linkSet.area[i].id == toid) {// to方向を検索
				index = i;
				break;
			}
		}
		if (index < 0) {// エラー
			System.out.println("Link area error : " + PanoId.elementAt(point) + " - " + PanoId.elementAt(toid));
		}
		return index;
	}

	// 向きを取得する
	int getMuki(int index, int gyaku) {
		int m = 0;
		if (index >= 0) {
			int sx = links[point].linkSet.area[index].sx;
			int ex = links[point].linkSet.area[index].ex;
			if ((gyaku == 0 && sx <= ex) || (gyaku != 0 && sx > ex)) {// 通常向き
				m = ((sx + ex) / 2) % pano_w;
			} else {// 逆向き
				m = ((sx + ex + pano_w) / 2) % pano_w;
			}
		}
		return m;
	}

	// 座標（ｘ，ｙ）がリンク内にあるか調べる
	public int linkCheck(int x, int y) {
		int sx, sy, ex, ey;
		int check = -1;
		for (int i=0; i<links[point].linkSet.area.length; i++) {
			sx = links[point].linkSet.area[i].sx % pano_w;
			ex = links[point].linkSet.area[i].ex % pano_w;
			sy = links[point].linkSet.area[i].sy;
			ey = links[point].linkSet.area[i].ey;
			if (sx <= ex && x < ex && x >= sx && y < ey && y >= sy) {
				check = i;
			}
			if (sx > ex && (x < ex || x >= sx) && y < ey && y >= sy) {
				check = i;
			}
		}
		return check;
	}

	// 画像上のｘ→表示上のｘを求める
	public int dispX(int pw, int x) {
		int dispx = -1;
		int prw = PANOW*100/panorate;// 表示サイズに表示可能なパノラマサイズ
		x = x % pw;
		if (muki < prw/2) {
			if (x >= pw+muki-prw/2 && x <= pw) {
				dispx = PMX + (-muki-pw+x)*panorate/100;
			} else if (x >= 0 && x <= muki+prw/2) {
				dispx = PMX + (-muki+x)*panorate/100;
			}
		} else if (muki < pw-prw/2) {
			if (x >= muki-prw/2 && x <= muki+prw/2) {
				dispx = PMX + (-muki+x)*panorate/100;
			}
		} else if (muki < pw) {
			if (x >= muki-prw/2 && x <= pw) {
				dispx = PMX + (-muki+x)*panorate/100;
			} else if (x >= 0 && x <= muki-pw+prw/2) {
				dispx = PMX + (-muki+pw+x)*panorate/100;
			}
		}
		return dispx;
	}

	// 画像上のｙ→表示上のｙを求める
	// 戻り値 -1:小さい方向で範囲外、PANOY1+PANOH+1:大きい方向で範囲外
	public int dispY(int ph, int y) {
		int dispy = -1;
		int prh = PANOH*100/panorate;// 表示サイズに表示可能なパノラマサイズ
		int pnh = ph*panorate/100;// 必要な表示サイズ
		int qy1 = Math.max(0, (ph-prh)/2);// 表示パノラマｙ１
		int qy2 = Math.min(ph, qy1 + prh);// 表示パノラマｙ２
		if (y >= qy1 && y < qy2) {
			dispy = PMY-pnh/2 + y*panorate/100;
		} else if (y >= qy2) {
			dispy = PANOY1+PANOH+1;
		}
		return dispy;
	}

	// 表示上のｘ→画像上のｘを求める
	// 戻り値 -1:画像上にない
	public int imgX(int pw,int x) {
		int imgx = -1;
		int prw = PANOW*100/panorate;// 表示サイズに表示可能なパノラマサイズ
		int pnw = pw*panorate/100;// 必要な表示サイズ
		int pnm = muki*panorate/100;// 必要な表示サイズ(0～muki)
		if (muki < prw/2) {
			if (x >= PX1 && x <= PMX-pnm) {
				imgx = pw + muki - (PMX-x)*100/panorate;
			} else if (x > PMX-pnm && x <= PX2) {
				imgx = muki - (PMX-x)*100/panorate;
			}
		} else if (muki < pw-prw/2) {
			if (x >= PX1 && x <= PX2) {
				imgx = muki - (PMX-x)*100/panorate;
			}
		} else if (muki < pw) {
			if (x >= PX1 && x <= PMX+pnw-pnm) {
				imgx = muki - (PMX-x)*100/panorate;
			} else if (x > PMX+pnw-pnm && x <= PX2) {
				imgx = muki - pw - (PMX-x)*100/panorate;
			}
		}
		return imgx;
	}

	// 表示上のｙ→画像上のｙを求める
	// 戻り値 -1:画像上にない
	public int imgY(int ph, int y) {
		int imgy = -1;
		int pnh = ph*panorate/100;// 必要な表示サイズ
		int py1 = Math.max(PANOY1, PMY-pnh/2);// 表示位置ｙ１
		int py2 = Math.min(PANOY1+PANOH, py1 + pnh);// 表示位置ｙ２
		if (y >= py1 && y < py2) {
			imgy = ph/2 + (y - PMY)*100/panorate;
		}
		return imgy;
	}

class LinkArea {
	int id;// パタパタ先ＩＤ(0～LINKIDMIN-1)／リンク番号(LINKIDMIN～WARPIDMIN-1)／ワープ番号(WARPIDMIN～)
	int sx;// 領域左上ｘ
	int sy;// 領域左上ｙ
	int ex;// 領域右下ｘ
	int ey;// 領域右下ｙ
	int data1;// パタパタ開始番号／リンクオブジェクト番号
	int data2;// パタパタ数／リンク先タイプ
	String data3;// 初期ＩＤ
	String data4;// 初期向き
	LinkArea(){}
	LinkArea(int id, int sx, int sy, int ex, int ey, int data1, int data2, String data3, String data4){
		this.id = id;
		this.sx = sx;
		this.sy = sy;
		this.ex = ex;
		this.ey = ey;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
		this.data4 = data4;
	}
}

class LinkSet {
	LinkArea[] area;
	Vector v;
	boolean linkExist;
	public LinkSet(Vector vv){
		v = new Vector();
		v = vv;
		area = new LinkArea[v.size()];
		//System.out.println("v.size()="+v.size());
		v.copyInto(area);
	}

	LinkSet(){}
}

class LinkDataSet{
	LinkSet linkSet;
	int PanoId;
	String PanoUri;
	LinkDataSet(LinkSet linkSet,int PanoId,String PanoUri){
		this.linkSet=linkSet;
		this.PanoId=PanoId;
		this.PanoUri=PanoUri;
	}
}

class Panorama {
	int id;
	Image img;
	int mx;
	int my;
	int angle;
	int skip;
	Panorama(int id, Image img, int mx, int my, int angle, int skip){
		this.id = id;
		this.img = img;
		this.mx = mx;
		this.my = my;
		this.angle = angle;
		this.skip = skip;
	}
}

class MapObj {
	int id;
	int mx;
	int my;
	Image icon;
	Image img;
	String txt;
	MapObj(int id, int mx, int my, Image icon, Image img, String txt){
		this.id = id;
		this.mx = mx;
		this.my = my;
		this.icon = icon;
		this.img = img;
		this.txt = txt;
	}
}

}
