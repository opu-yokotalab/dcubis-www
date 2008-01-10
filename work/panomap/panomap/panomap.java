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
	static int FRAMEW = 760;			// �t���[����
	static int FRAMEH = 640;			// �t���[������
	static int SPACEX = 0;				// ���̃X�y�[�X�w
	static int SPACEY = 0;				// ��̃X�y�[�X�x
	static int CANVASX = 0;				// �L�����o�X�w
	static int CANVASY = 25;			// �L�����o�X�x
	static int CANVASW = 760;			// �L�����o�X��
	static int CANVASH = 580;			// �L�����o�X����
	static int BTNY = 25;				// ��̃{�^���x

	static int pisize = 240;			// �p�m���}�\����T�C�Y
	static int misize = 240;			// �n�}�\����T�C�Y
	static int colayout = 0;			// �{�^���̃��C�A�E�g�i0:��A1:���A2:���A3:�E�j
	static int disptype = 0;			// �\����ʁi0:�W���A1:�g�o�A2:�n�}�A3:���ē��j
	static String scenariofile = "";	// �V�i���I�t�@�C��
	static int scenariono = -1;			// �V�i���I�ԍ�

	static int PANOY1 = 20;				// �p�m���}�\����ʒu��
	static int PANOW = 480;				// �p�m���}�\����
	static int PANOH = 240;				// �p�m���}�\������
	static int ARWW1 = 30;				// ���P�\����
	static int ARWW2 = 40;				// ���Q�\����
	static int ARWH = 30;				// ��󍂂�
	static int MAPY1 = 320;				// �n�}�\����ʒu��
	static int MAPW = 240;				// �n�}�\����
	static int MAPH = 240;				// �n�}�\������
	static int FUKIW = 160;				// �ӂ������\����
	static int FUKIH = 240;				// �ӂ������\������
	static int FUKIY1 = 320;			// �ӂ������\����

	static int MMX = CANVASW/2;			// �n�}�̒��S��
	static int MMY = MAPY1+MAPH/2;		// �n�}�̒��S��
	static int MX1 = MMX-MAPW/2;		// �n�}�̍��ʒu
	static int MX2 = MMX+MAPW/2;		// �n�}�̉E�ʒu
	static int PMX = CANVASW/2;			// �p�m���}�̒��S��
	static int PMY = PANOY1+PANOH/2;	// �p�m���}�̒��S��
	static int PX1 = PMX-PANOW/2;		// �p�m���}�̍��ʒu
	static int PX2 = PMX+PANOW/2;		// �p�m���}�̉E�ʒu

	static int MOIS = 28;				// �o�n�h�\���T�C�Y�i�A�C�R���j
	static int MPPS = 8;				// �n�}�p�m���}�|�C���g�\���T�C�Y�i���j
	int mois = MOIS;					// �o�n�h�\���T�C�Y�i�A�C�R���j
	int mpps = MPPS;					// �n�}�p�m���}�|�C���g�\���T�C�Y�i���j

	static int MIS = 10;				// ����
	static int ICONSIZE = 16;			// �}�b�v�A�C�R���T�C�Y

	static int PANO_ROTA_UNIT = 8;		// �p�m���}��]�P��
	static int PANO_ZOOM_UNIT = 10;		// �p�m���}�Y�[���P��
	static int PANO_ZOOM_MAX = 300;		// �p�m���}�Y�[���ő�
	static int PANO_ZOOM_MIN = 30;		// �p�m���}�Y�[���ŏ�
	static int MAP_MOVE_UNIT = 20;		// �n�}�ړ��P��
	static int MAP_ZOOM_UNIT = 10;		// �n�}�Y�[���P��
	static int MAP_ZOOM_MAX = 300;		// �n�}�Y�[���ő�
	static int MAP_ZOOM_MIN = 20;		// �n�}�Y�[���ŏ�
	static int MAP_ROTA_UNIT = 30;		// �n�}��]�P��
	static int LINKIDMIN = 1000;		// �����N�h�c�J�n�l
	static int WARPIDMIN = 2000;		// ���[�v�h�c�J�n�l
	static int CONDS_MAX = 4;			// �����ő吔
	static int COND_MAX = 8;			// �����̍��ڍő吔
	static String INITDATA_XML = "initdata.xml";// �����f�[�^
	static String SSBTNLABEL1 = "�V�i���I�J�n";// �V�i���I�J�n�{�^�����x��
	static String SSBTNLABEL2 = "�ĊJ";// �V�i���I�J�n�{�^�����x��
	static String SSBTNLABEL3 = "�ꎞ��~";// �V�i���I�J�n�{�^�����x��
	static String SCBTNLABEL1 = "�R�}����";// �R�}����{�^�����x��
	static String SCBTNLABEL2 = "�R�}����";// �R�}����{�^�����x��
	static String SCBTNLABEL3 = "���~";// �R�}����{�^�����x��
	static int DEF_DELAY2 = 200;		// �^�C�}�[�Q�x��(ms)
	static int DEF_PERIOD2 = 50;		// �^�C�}�[�Q�Ԋu(ms)
	static String[] BTNMSG = {"","�p�m���}�F�R�O�x�����������܂��B","�p�m���}�F���������������܂��B",
							"�p�m���}�F�������E�������܂��B","�p�m���}�F�R�O�x���E�������܂��B",
							"�p�m���}�F�k���\�����܂��B","�p�m���}�F�g��\�����܂��B",
							"�n�}�F���ֈړ����܂��B","�n�}�F�E�ֈړ����܂��B",
							"�n�}�F��ֈړ����܂��B","�n�}�F���ֈړ����܂��B",
							"�n�}�F�k���\�����܂��B","�n�}�F�g��\�����܂��B",
							"�n�}�F���ɂR�O�x����]���܂��B","�n�}�F�E�ɂR�O�x����]���܂��B"};

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
	Button btn1 = new Button("�ǂݍ���");
	Button btn2 = new Button("�����ʒu");
	Button SRBtn = new Button("�V�i���I�Ǎ�");
	Button SSBtn = new Button(SSBTNLABEL1);
	Button SCBtn = new Button(SCBTNLABEL1);
	Label label1 = new Label();
	Label label2 = new Label();

	MediaTracker mt;
	int mtid = 0;
	String mapxmlfilename = "";			// �n�}�f�[�^�w�l�k
	String map_filename = "";			// �n�}�摜�t�@�C��
	static String initidparam = "";		// �����n�_(�p�����^)
	static String initmukiparam = "";	// ��������(�p�����^)
	String initidstring = "";			// �����n�_(String)
	String initmukistring = "";			// ��������(String)
	Image img_map;						// �n�}
	Image img_arwicon;					// ���A�C�R��
	Image img_mapicon;					// �n�}����A�C�R��
	Image img_guide;					// �K�C�h
	Image img_logo;						// ���S
	int mapw = 0;						// �n�}��
	int maph = 0;						// �n�}����
	int mapx = 120;						// �n�}�\���ʒu
	int mapy = 120;						// �n�}�\���ʒu
	int mapa = 0;						// �n�}�\���p�x
	int maprate = 100;					// �n�}�\���{���i���j
	int initpoint = 0;					// �����n�_
	int point = 0;						// ���ݒn�_
	int backpoint = 0;					// ���O�n�_
	int initmuki = 120;					// ��������
	int muki = 120;						// ���݌���
	int backmuki = 120;					// ���O����

	Timer tm1;							// �^�C�}�[�P�i�ړ��p�j
	Timer tm2;							// �^�C�}�[�Q�i�p�m���}�A�n�}����p�j
	TimerTask1 task1 = null;			// �^�C�}�[�P�^�X�N
	TimerTask2 task2 = null;			// �^�C�}�[�Q�^�X�N
	long period1 = 500;					// �^�C�}�[�P�Ԋu(ms)
	long period2 = 50;					// �^�C�}�[�Q�Ԋu(ms)
	LinkDataSet[] links;
	LinkSet[] link;
	Vector v,PanoId,PanoUri,PanoMx,PanoMy,PanoAngle,PanoSkip,PataUri,LinkUri,LinkId,WarpId;
	Vector FpoiMx,FpoiMy,FpoiIcon,FpoiImg,FpoiTxt,NpoiMx,NpoiMy,NpoiIcon;
	Vector Scene,Mapxml;
	Vector[] Cond = new Vector[CONDS_MAX];
	String[] Condkey = new String[CONDS_MAX];
	Panorama[] Pano;
	MapObj[] Fpoi;						// �ӂ������o�n�h
	MapObj[] Npoi;						// ���O�o�n�h
	Image[] PataImg;
	String[] LinkObj;
	StringBuffer scene = new StringBuffer("");
	String scenemsg1 = "";				// �V�i���I��]�����b�Z�[�W
	String scenemsg2 = "";				// �V�i���I���i�����b�Z�[�W
	int fpoi_no = -1;					// �ӂ������o�n�h�ԍ�
	int fpoi_mode = 0;					// �ӂ������Œ胂�[�h
	int pata_mode = 0;					// �p�^�p�^���[�h(1:�p�^�p�^����,2:�p�^�p�^,3:��],4:�ꎞ��~����,5:�ꎞ��~,6:�I������)
	int pata_from = 0;					// �p�^�p�^�J�n�ԍ�
	int pata_now = 0;					// �p�^�p�^���ݔԍ�
	int pata_to = 0;					// �p�^�p�^�I���ԍ�
	int pata_index = 0;					// �p�^�p�^�̈�C���f�b�N�X
	int pata_muki = 0;					// �p�^�p�^�ڕW����
	int pata_speed = 1;					// �p�^�p�^�X�s�[�h
	int rota_mode = 0;					// ��]���[�h
	int d_muki = 0;						// ��]�̑���
	int waku_mode = 1;					// �g�\�����[�h
	int waku_no = -1;					// �g�\���ԍ�
	int button_no = -1;					// �{�^�������\���ԍ�
	int map_mode = 0;					// �n�}���샂�[�h
	int d_mapx = 0;						// �ړ��̑���
	int d_mapy = 0;						// �ړ��̑���
	int d_maprate = 0;					// �g��k���̑���
	int d_mapa = 0;						// ��]�̑���
	int pano_w = 0;						// �p�m���}��
	int pano_h = 0;						// �p�m���}����
	int pano_a = 0;						// �p�m���}�J�n�p�x
	int panorate = 100;					// �p�m���}�\���{���i���j
	int d_panorate = 0;					// �g��k���̑���
	int map_imgmode = 1;				// �ʐ^�\�����[�h
	int map_centermode = 0;				// ���݈ʒu���S�Œ胂�[�h
	int map_upmode = 0;					// ���݌�����Œ胂�[�h
	int map_popmode = 0;				// �ӂ��������[�h
	int map_scemode = 0;				// �V�i���I���[�h
	int map_dispamode = 1;				// ���p�\�����[�h
	int map_disprmode = 0;				// �S���[�g�\�����[�h
	int cursor = 0;						// �J�[�\���`��
	int norepaint = 0;					// �ĕ`�悵�Ȃ��t���O
	int dragsx = 0;						// �h���b�O�J�n��
	Color bcolor = Color.black;			// �w�i�J���[
	Color fcolor = Color.white;			// �����J���[
	Color wcolor = Color.darkGray;		// ���N�J���[
	Color hcolor = new Color(255,255,225);	// �ӂ������J���[
	Color pbcolor = Color.lightGray;	// �p�l���w�i�J���[
	Color pfcolor = Color.black;		// �p�l�������J���[
	TextPlayer textPlayer;
	PicturePlayer picturePlayer;
	final static BasicStroke stroke1 = new BasicStroke(1.0f);
	final static BasicStroke stroke2 = new BasicStroke(2.0f);

	class TestCanvas extends Canvas {
		BufferedImage back_image = null;	// �o�b�t�@
		Graphics2D back_g;					// �o�b�t�@��Graphics�I�u�W�F�N�g

		public void paint(Graphics g) {

		if (norepaint == 1) return;

		Graphics2D g2 = (Graphics2D) g;

		if (back_image == null) {
			back_image = (BufferedImage)createImage(getSize().width,getSize().height);
			back_g = (Graphics2D)back_image.createGraphics();
		}

		if (Pano[point].img == null) {// �����[�h�Ȃ烍�[�h����
			Pano[point].img = LoadImage(PanoUri.elementAt(point).toString());
		}

		pano_w = Pano[point].img.getWidth(this);
		pano_h = Pano[point].img.getHeight(this);

//		if (rota_mode == 0) {// �p�m���}��]���ȊO
			back_g.setColor(bcolor);// �w�i�J���[
			back_g.fillRect(0, PANOY1+PANOH+ICONSIZE, CANVASW, BTNY+MIS*4+MAPH);// �h��Ԃ�
			paintMap(g2);// �n�}��`��
//		}
		if (disptype != 2) {
			back_g.setColor(bcolor);// �w�i�J���[
			back_g.fillRect(0, PANOY1-ICONSIZE, CANVASW, PANOH+ICONSIZE*2);// �h��Ԃ�
			paintPano(g2);// �p�m���}��`��
		}

		g2.drawImage(back_image,0,0,this);

		}// end of paint()

		public void paintMap(Graphics2D g2) {

		// �n�}��`��
		back_g.setClip(MX1, MAPY1, MAPW, MAPH);// �N���b�v�̈���w��
		if (map_centermode == 1) {// ���݈ʒu���S�Œ胂�[�h
			if (pata_mode != 2) {// �p�m���}
				mapx = Pano[point].mx;
				mapy = Pano[point].my;
			} else {// �p�^�p�^
				mapx = Pano[backpoint].mx + (Pano[point].mx-Pano[backpoint].mx)*(pata_now-pata_from+1)/(pata_to-pata_from+2);
				mapy = Pano[backpoint].my + (Pano[point].my-Pano[backpoint].my)*(pata_now-pata_from+1)/(pata_to-pata_from+2);
			}
		}
		if (map_upmode == 1) {// ���݌�����Œ胂�[�h
			mapa = (Pano[point].angle - muki*360/pano_w + 270 + 360) % 360;// �p�x�𒲐�
		}
		int drx = MAPW/2;// �\���T�C�Y�̔���
		int dry = MAPH/2;// �\���T�C�Y�̔���
		int mrx = drx*100/maprate;// �\���T�C�Y�̔����ɕ\���\�Ȓn�}�T�C�Y
		int mry = dry*100/maprate;// �\���T�C�Y�̔����ɕ\���\�Ȓn�}�T�C�Y
		BufferedImage map_image = (BufferedImage)createImage(MAPW*3/2, MAPH*3/2);// �܂��P�D�T�{�ŕ`���A�^�񒆂����g��
		Graphics2D map_g = (Graphics2D)map_image.createGraphics();
		map_g.rotate(mapa*Math.PI/180.0, MAPW*3/4, MAPH*3/4);// ���S�ŉ�]
		int msx = Math.max(0, mrx*3/2-mapx);// �n�}��\�������i���������j�̒n�}�T�C�Y
		int msy = Math.max(0, mry*3/2-mapy);// �n�}��\�������i���������j�̒n�}�T�C�Y
		int dsx = msx*maprate/100;// �n�}��\�������i���������j�̕\���T�C�Y
		int dsy = msy*maprate/100;// �n�}��\�������i���������j�̕\���T�C�Y
		map_g.drawImage(img_map, dsx, dsy, MAPW*3/2, MAPH*3/2, mapx-mrx*3/2+msx, mapy-mry*3/2+msy, mapx+mrx*3/2, mapy+mry*3/2, this);// �n�}�摜�\��
		back_g.drawImage(map_image, MX1, MAPY1, MX2, MAPY1+MAPH, MAPW/4, MAPH/4, MAPW*5/4, MAPH*5/4, this);// �n�}�摜�\��
//		back_g.drawImage(img_map, MX1, MAPY1, MX2, MAPY1+MAPH, mapx-mrx, mapy-mry, mapx+mrx, mapy+mry, this);// �n�}�摜�\��
		map_g.dispose();
		map_image.flush();
		// �����|�C���g�A�����摜��`��
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
			ddx = Calc_ddx(Fpoi[i].mx, Fpoi[i].my);// �\�����S����̑��Έʒu
			ddy = Calc_ddy(Fpoi[i].mx, Fpoi[i].my);// �\�����S����̑��Έʒu
			if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �\���͈͓�
				if (map_imgmode == 0) {// �ʐ^��\��
					back_g.setColor(Color.blue);// ��
					back_g.fillOval(MMX+ddx-mpps, MMY+ddy-mpps, mpps*2, mpps*2);// �n�}��Ɍ����|�C���g�\��
					back_g.setColor(Color.black);// ��
					back_g.drawOval(MMX+ddx-mpps, MMY+ddy-mpps, mpps*2, mpps*2);// �n�}��Ɍ����|�C���g�\��
				} else {// �ʐ^�\��
					if (Fpoi[i].icon == null) {// �����[�h�Ȃ烍�[�h����
						Fpoi[i].icon = LoadImage(FpoiIcon.elementAt(i).toString());
					}
					back_g.drawImage(Fpoi[i].icon, MMX+ddx-mois, MMY+ddy-mois, mois*2, mois*2, this);// �n�}��Ɍ����摜�\��
				}
			} else {// �\���͈͊O
				continue;
			}
			if (map_popmode == 1) {// �ӂ��������[�h
				if (ddx >= 0 && ddy < 0) {// �E��͈͓�
					int dist = ddx*ddx + ddy*ddy;// �����̂Q��
					if (dist < robj_dist) {
						robj_id = i;
						robj_dist = dist;
						robj_ddx = ddx;
						robj_ddy = ddy;
					}
				} else if (ddx < 0 && ddy < 0) {// ����͈͓�
					int dist = ddx*ddx + ddy*ddy;// �����̂Q��
					if (dist < lobj_dist) {
						lobj_id = i;
						lobj_dist = dist;
						lobj_ddx = ddx;
						lobj_ddy = ddy;
					}
				}
			}
			if (map_popmode == 0 && fpoi_no == i) {
				if (disptype == 2 || disptype == 3) {// �n�}���C�A�E�g�܂��͓��ē����C�A�E�g
					lobj_id = i;
					lobj_ddx = ddx;
					lobj_ddy = ddy;
				} else {
					if (ddx >= 0) {// �E�͈͓�
						robj_id = i;
						robj_ddx = ddx;
						robj_ddy = ddy;
					} else {// ���͈͓�
						lobj_id = i;
						lobj_ddx = ddx;
						lobj_ddy = ddy;
					}
				}
			}
		}
		// ���O�o�n�h��`��
		for (int i=0; i<Npoi.length; i++) {
			ddx = Calc_ddx(Npoi[i].mx, Npoi[i].my);// �\�����S����̑��Έʒu
			ddy = Calc_ddy(Npoi[i].mx, Npoi[i].my);// �\�����S����̑��Έʒu
			if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �\���͈͓�
				if (Npoi[i].icon == null) {// �����[�h�Ȃ烍�[�h����
					Npoi[i].icon = LoadImage(NpoiIcon.elementAt(i).toString());
				}
				back_g.drawImage(Npoi[i].icon, MMX+ddx-Npoi[i].icon.getWidth(this)/2, MMY+ddy-Npoi[i].icon.getHeight(this)/2, this);// �n�}��ɖ��O�o�n�h�\��
			}
		}
		// �p�m���}�|�C���g��`��
		back_g.setColor(Color.black);// ��
		for (int i=0; i<Pano.length; i++) {
			ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
			ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
			if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �\���͈͓�
				back_g.drawOval(MMX+ddx-mpps, MMY+ddy-mpps, mpps*2, mpps*2);// �n�}��Ƀp�m���}�|�C���g�\��
			}
		}
		// �S���[�g��`��
		if (map_disprmode == 1) {// �S���[�g�\�����[�h
			int ddx2, ddy2;
			back_g.setColor(Color.black);// ��
			for (int i=0; i<Pano.length; i++) {
				ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
				ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
				for (int j=0; j<links[i].linkSet.area.length; j++){// �g�\�����[�v
					LinkArea la = links[i].linkSet.area[j];
					if (la.id >= 0 && la.id <= LINKIDMIN-1) {// �p�^�p�^�ւ̃����N
						ddx2 = Calc_ddx(Pano[la.id].mx, Pano[la.id].my);// �\�����S����̑��Έʒu
						ddy2 = Calc_ddy(Pano[la.id].mx, Pano[la.id].my);// �\�����S����̑��Έʒu
						if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry ||
							-drx < ddx2 && ddx2 < drx && -dry < ddy2 && ddy2 < dry) {// �ǂ��炩���\���͈͓�
							back_g.drawLine(MMX+ddx, MMY+ddy, MMX+ddx2, MMY+ddy2);// �n�}��Ƀ��[�g�\��
						}
//						if (-drx < ddx2 && ddx2 < drx && -dry < ddy2 && ddy2 < dry) {// �悪�\���͈͓�
//							int ar = 8;// �T�C�Y
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
		// �V�i���I���[�g��`��
		if (map_scemode == 1) {// �V�i���I���[�h
			back_g.setStroke(stroke2);
			int ddx0 = 9999;
			int ddy0 = 9999;
			String sce = (String)Scene.elementAt(choScena.getSelectedIndex());
			StringTokenizer st = new StringTokenizer(sce, ";");
			back_g.setColor(Color.blue);// ��
			while (st.hasMoreTokens()) {
				String nextstr = st.nextToken();
				nextstr = nextstr.substring(0,nextstr.indexOf(','));
				if (nextstr.equals("stop")) continue;
//				int spoint = Integer.parseInt(nextstr);
				int spoint = getId(nextstr);// �h�c�ԍ����擾
				if (spoint < 0) break;
				if (spoint >= LINKIDMIN) continue;
				ddx = Calc_ddx(Pano[spoint].mx, Pano[spoint].my);// �\�����S����̑��Έʒu
				ddy = Calc_ddy(Pano[spoint].mx, Pano[spoint].my);// �\�����S����̑��Έʒu
				if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �\���͈͓�
					back_g.drawOval(MMX+ddx-mpps, MMY+ddy-mpps, mpps*2, mpps*2);// �n�}��ɃV�i���I�|�C���g�\��
				}
				if (ddx0 != 9999 && ddy0 != 9999) {// �ŏ��ȊO
					back_g.drawLine(MMX+ddx, MMY+ddy, MMX+ddx0, MMY+ddy0);// �n�}��ɃV�i���I���[�g�\��
				}
				ddx0 = ddx;
				ddy0 = ddy;
			}
			back_g.setStroke(stroke1);
		}
		// �ړ��̂�`��
		if (disptype != 2) {// �n�}���C�A�E�g�ȊO
			int mx, my;
			if (pata_mode != 2) {// �p�m���}
				mx = Pano[point].mx;
				my = Pano[point].my;
			} else {// �p�^�p�^
				mx = Pano[backpoint].mx + (Pano[point].mx-Pano[backpoint].mx)*(pata_now-pata_from+1)/(pata_to-pata_from+2);
				my = Pano[backpoint].my + (Pano[point].my-Pano[backpoint].my)*(pata_now-pata_from+1)/(pata_to-pata_from+2);
			}
			ddx = Calc_ddx(mx, my);// �\�����S����̑��Έʒu
			ddy = Calc_ddy(mx, my);// �\�����S����̑��Έʒu
			if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �\���͈͓�
				int pr = 5;// �ړ��̃T�C�Y
				int angle = mapa - Pano[point].angle + muki*360/pano_w;
				int dax1 = (int)(Math.cos(angle*Math.PI/180.0)*pr*3);
				int day1 = (int)(Math.sin(angle*Math.PI/180.0)*pr*3);
				int dax2 = (int)(Math.cos((angle+90)*Math.PI/180.0)*pr);
				int day2 = (int)(Math.sin((angle+90)*Math.PI/180.0)*pr);
				int dax3 = (int)(Math.cos((angle-90)*Math.PI/180.0)*pr);
				int day3 = (int)(Math.sin((angle-90)*Math.PI/180.0)*pr);
				int polyx[] = {MMX+ddx+dax1, MMX+ddx+dax2, MMX+ddx+dax3};
				int polyy[] = {MMY+ddy+day1, MMY+ddy+day2, MMY+ddy+day3};
				back_g.setColor(Color.red);// ��
				back_g.fillOval(MMX+ddx-pr, MMY+ddy-pr, pr*2, pr*2);// ���݃|�C���g�\���i�~�j
				back_g.fillPolygon(polyx, polyy, 3);// ���݃|�C���g�\���i�O�p�`�j
			}
		}
		// ���p�\����`��
		if (map_dispamode == 1) {// ���p�\�����[�h
			int ar = 5;// �T�C�Y
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
			back_g.setColor(Color.lightGray);// �O���[
			back_g.fillOval(MX2-ar*6, MAPY1, ar*6, ar*6);// ���p�\���i�~�j
			back_g.setColor(Color.red);// ��
			back_g.fillPolygon(polyx1, polyy1, 3);// ���p�\���i�O�p�`�j
			back_g.setColor(Color.white);// ��
			back_g.fillPolygon(polyx2, polyy2, 3);// ���p�\���i�O�p�`�j
			back_g.setColor(Color.black);// ��
			back_g.drawOval(MX2-ar*6, MAPY1, ar*6, ar*6);// ���p�\���i�~�j
			back_g.drawPolygon(polyx3, polyy3, 4);// ���p�\���i�l�p�`�j
		}
		back_g.setClip(null);// �N���b�v�̈������
		// �n�}�܂��̖���`��
		back_g.setColor(wcolor);// ���N�J���[
		if (disptype != 2) {// �n�}���C�A�E�g�ȊO
			back_g.fill3DRect(MX1-ICONSIZE, MAPY1+MAPH, ICONSIZE, ICONSIZE, true); // �����l�p
			back_g.drawImage(img_mapicon, MX1-ICONSIZE, MAPY1+MAPH, MX1, MAPY1+MAPH+ICONSIZE, 0, 32, 16, 48, this);// �k���摜�\��
			back_g.fill3DRect(MX2, MAPY1+MAPH, ICONSIZE, ICONSIZE, true); // �E���l�p
			back_g.drawImage(img_mapicon, MX2, MAPY1+MAPH, MX2+ICONSIZE, MAPY1+MAPH+ICONSIZE, 32, 32, 48, 48, this);// �g��摜�\��
		} else {
			back_g.fill3DRect(MX1-MIS, MAPY1+MAPH, MIS, MIS, true); // �����l�p
			back_g.fill3DRect(MX2, MAPY1+MAPH, MIS, MIS, true); // �E���l�p
		}
		if (disptype != 2 && map_upmode == 0) {// �n�}���C�A�E�g�ȊO����Œ�łȂ�
			back_g.fill3DRect(MX1-ICONSIZE, MAPY1-ICONSIZE, ICONSIZE, ICONSIZE, true); // ����l�p
			back_g.drawImage(img_mapicon, MX1-ICONSIZE, MAPY1-ICONSIZE, MX1, MAPY1, 0, 0, 16, 16, this);// ����]�摜�\��
			back_g.fill3DRect(MX2, MAPY1-ICONSIZE, ICONSIZE, ICONSIZE, true); // �E��l�p
			back_g.drawImage(img_mapicon, MX2, MAPY1-ICONSIZE, MX2+ICONSIZE, MAPY1, 32, 0, 48, 16, this);// �E��]�摜�\��
		} else {
			back_g.fill3DRect(MX1-MIS, MAPY1-MIS, MIS, MIS, true); // ����l�p
			back_g.fill3DRect(MX2, MAPY1-MIS, MIS, MIS, true); // �E��l�p
		}
		back_g.setClip(MX1-1, MAPY1-MIS, MAPW+2, MIS);// �N���b�v�̈���w��
		back_g.fill3DRect(MX1-MIS, MAPY1-MIS, MAPW+MIS*2, MIS, true); // �n�}��l�p
		back_g.setClip(MX1-1, MAPY1+MAPH, MAPW+2, MIS);// �N���b�v�̈���w��
		back_g.fill3DRect(MX1-MIS, MAPY1+MAPH, MAPW+MIS*2, MIS, true); // �n�}���l�p
		back_g.setClip(MX1-MIS, MAPY1-1, MIS, MAPH+2);// �N���b�v�̈���w��
		back_g.fill3DRect(MX1-MIS, MAPY1-2, MIS, MAPH+4, true); // �n�}���l�p
		back_g.setClip(MX2, MAPY1-1, MIS, MAPH+2);// �N���b�v�̈���w��
		back_g.fill3DRect(MX2, MAPY1-2, MIS, MAPH+4, true); // �n�}�E�l�p
		if (disptype != 2 && map_centermode == 0) {// �n�}���C�A�E�g�ȊO�����S�Œ�łȂ�
			back_g.setClip(MX1-ICONSIZE, MMY-ICONSIZE/2, ICONSIZE-MIS+1, ICONSIZE);// �N���b�v�̈���w��
			back_g.fill3DRect(MX1-ICONSIZE, MMY-ICONSIZE/2, ICONSIZE, ICONSIZE, true); // �n�}���l�p
			back_g.setClip(MX2+MIS-1, MMY-ICONSIZE/2, ICONSIZE-MIS+1, ICONSIZE);// �N���b�v�̈���w��
			back_g.fill3DRect(MX2, MMY-ICONSIZE/2, ICONSIZE, ICONSIZE, true); // �n�}���l�p
			back_g.setClip(MMX-ICONSIZE/2, MAPY1-ICONSIZE, ICONSIZE, ICONSIZE-MIS+1);// �N���b�v�̈���w��
			back_g.fill3DRect(MMX-ICONSIZE/2, MAPY1-ICONSIZE, ICONSIZE, ICONSIZE, true); // �n�}���l�p
			back_g.setClip(MMX-ICONSIZE/2, MAPY1+MAPH+MIS-1, ICONSIZE, ICONSIZE-MIS+1);// �N���b�v�̈���w��
			back_g.fill3DRect(MMX-ICONSIZE/2, MAPY1+MAPH, ICONSIZE, ICONSIZE, true); // �n�}���l�p
			back_g.setClip(null);// �N���b�v�̈������
			back_g.drawImage(img_mapicon, MX1-ICONSIZE, MMY-ICONSIZE/2, MX1, MMY+ICONSIZE/2, 0, 16, 16, 32, this);// �����摜�\��
			back_g.drawImage(img_mapicon, MX2, MMY-ICONSIZE/2, MX2+ICONSIZE, MMY+ICONSIZE/2, 32, 16, 48, 32, this);// �E���摜�\��
			back_g.drawImage(img_mapicon, MMX-ICONSIZE/2, MAPY1-ICONSIZE, MMX+ICONSIZE/2, MAPY1, 16, 0, 32, 16, this);// ����摜�\��
			back_g.drawImage(img_mapicon, MMX-ICONSIZE/2, MAPY1+MAPH, MMX+ICONSIZE/2, MAPY1+MAPH+ICONSIZE, 16, 32, 32, 48, this);// �����摜�\��
		} else {
			back_g.setClip(null);// �N���b�v�̈������
		}
		// �e�L�X�g�G���A��\��
		if (map_popmode == 0 && fpoi_no < 0) {
			if (textArea1.isVisible()) textArea1.setVisible(false);
			if (textArea2.isVisible()) textArea2.setVisible(false);
		}
		// �ӂ�������`��
		if (robj_id >= 0) {// �E�ɃI�u�W�F�N�g������H
			int poly_x[] = {MMX+robj_ddx, MX2+MIS*2+1, MX2+MIS*2+1};
			int poly_y[] = {MMY+robj_ddy, FUKIY1+FUKIW/2+MIS, FUKIY1+FUKIW/2-MIS};
			back_g.setColor(hcolor);// �ӂ������J���[
			back_g.fillRoundRect(MX2+MIS*2, FUKIY1, FUKIW, FUKIH, MIS*2, MIS*2);// �t�L�_�V�i�p�ێl�p�j�\��
			back_g.setColor(Color.black);// ��
			back_g.drawRoundRect(MX2+MIS*2, FUKIY1, FUKIW, FUKIH, MIS*2, MIS*2);// �t�L�_�V�i�p�ێl�p�j�\��
			back_g.setColor(hcolor);// �ӂ������J���[
			back_g.fillPolygon(poly_x, poly_y, 3);// �t�L�_�V�i�O�p�j�\��
			back_g.setColor(Color.black);// ��
			back_g.drawLine(poly_x[0], poly_y[0], poly_x[1], poly_y[1]);// �t�L�_�V�i���j�\��
			back_g.drawLine(poly_x[0], poly_y[0], poly_x[2], poly_y[2]);// �t�L�_�V�i���j�\��
			if (FpoiImg.elementAt(robj_id).toString().length() > 0) {
				if (Fpoi[robj_id].img == null) {// �����[�h�Ȃ烍�[�h����
					Fpoi[robj_id].img = LoadImage(FpoiImg.elementAt(robj_id).toString());
				}
				back_g.drawImage(Fpoi[robj_id].img, MX2+MIS*3, FUKIY1+MIS, FUKIW-MIS*2, FUKIW-MIS*2, this);// �����摜�\��
			}
			if (FpoiTxt.elementAt(robj_id).toString().length() > 0) {
				if (Fpoi[robj_id].txt == null) {// �����[�h�Ȃ烍�[�h����
					Fpoi[robj_id].txt = LoadText(FpoiTxt.elementAt(robj_id).toString());
				}
				if (!textArea2.isVisible()) textArea2.setVisible(true);
				textArea2.setText(Fpoi[robj_id].txt);// ���������\��
			}
		} else {
			if (textArea2.isVisible()) textArea2.setVisible(false);
		}
		if (lobj_id >= 0) {// ���ɃI�u�W�F�N�g������H
			int poly_x[] = {MMX+lobj_ddx, MX1-MIS*2, MX1-MIS*2};
			int poly_y[] = {MMY+lobj_ddy, FUKIY1+FUKIW/2+MIS, FUKIY1+FUKIW/2-MIS};
			back_g.setColor(hcolor);// �ӂ������J���[
			back_g.fillRoundRect(MX1-MIS*2-FUKIW, FUKIY1, FUKIW, FUKIH, MIS*2, MIS*2);// �t�L�_�V�i�p�ێl�p�j�\��
			back_g.setColor(Color.black);// ��
			back_g.drawRoundRect(MX1-MIS*2-FUKIW, FUKIY1, FUKIW, FUKIH, MIS*2, MIS*2);// �t�L�_�V�i�p�ێl�p�j�\��
			back_g.setColor(hcolor);// �ӂ������J���[
			back_g.fillPolygon(poly_x, poly_y, 3);// �t�L�_�V�i�O�p�j�\��
			back_g.setColor(Color.black);// ��
			back_g.drawLine(poly_x[0], poly_y[0], poly_x[1], poly_y[1]);// �t�L�_�V�i���j�\��
			back_g.drawLine(poly_x[0], poly_y[0], poly_x[2], poly_y[2]);// �t�L�_�V�i���j�\��
			if (FpoiImg.elementAt(lobj_id).toString().length() > 0) {
				if (Fpoi[lobj_id].img == null) {// �����[�h�Ȃ烍�[�h����
					Fpoi[lobj_id].img = LoadImage(FpoiImg.elementAt(lobj_id).toString());
				}
				back_g.drawImage(Fpoi[lobj_id].img, MX1-MIS*2-FUKIW+MIS, FUKIY1+MIS, FUKIW-MIS*2, FUKIW-MIS*2, this);// �����摜�\��
			}
			if (FpoiTxt.elementAt(lobj_id).toString().length() > 0) {
				if (Fpoi[lobj_id].txt == null) {// �����[�h�Ȃ烍�[�h����
					Fpoi[lobj_id].txt = LoadText(FpoiTxt.elementAt(lobj_id).toString());
				}
				if (!textArea1.isVisible()) textArea1.setVisible(true);
				textArea1.setText(Fpoi[lobj_id].txt);// ���������\��
			}
		} else {
			if (textArea1.isVisible()) textArea1.setVisible(false);
		}

		}// end of paintMap()

		public void paintPano(Graphics2D g2) {

		// �p�m���}�^�p�^�p�^��`��
		String charahuki = "";
		String label2text = "";
		if (pata_mode > 0) {// 
			if (task1 != null) {
				if (SCBtn.getLabel().equals(SCBTNLABEL3)) {
					if (pata_mode == 3 && !scenemsg1.equals("")) {// ��]���[�h
						label2text = scenemsg1;// �V�i���I��]�����b�Z�[�W��\��
						charahuki = scenemsg1;// �V�i���I��]�����b�Z�[�W��\��
					} else if (pata_mode == 2 && !scenemsg2.equals("")) {// �p�^�p�^���[�h
						label2text = scenemsg2;// �V�i���I���i�����b�Z�[�W��\��
						charahuki = scenemsg2;// �V�i���I���i�����b�Z�[�W��\��
					} else {
//						label2text = "�V�i���I���s���B";
					}
				}
			} else {
//				label2text = "�V�i���I�ꎞ��~���B";
				if (pata_mode == 3 && !scenemsg1.equals("")) {// ��]���[�h
					charahuki = scenemsg1;// �V�i���I��]�����b�Z�[�W��\��
				} else if (pata_mode == 2 && !scenemsg2.equals("")) {// �p�^�p�^���[�h
					charahuki = scenemsg2;// �V�i���I���i�����b�Z�[�W��\��
				} else if (pata_mode == 4) {// �ꎞ��~�������[�h
					if (!scenemsg2.equals("")) {
						charahuki = scenemsg2;// �V�i���I���i�����b�Z�[�W��\��
					} else if (!scenemsg1.equals("")) {
						charahuki = scenemsg1;// �V�i���I��]�����b�Z�[�W��\��
					}
					scenemsg1 = "";
					scenemsg2 = "";
					pata_mode = 5;
				} else if (pata_mode == 5) {// �ꎞ��~���[�h
				} else if (pata_mode == 6) {// �I���������[�h
					if (!scenemsg2.equals("")) {
						charahuki = scenemsg2;// �V�i���I���i�����b�Z�[�W��\��
					} else if (!scenemsg1.equals("")) {
						charahuki = scenemsg1;// �V�i���I��]�����b�Z�[�W��\��
					}
					scenemsg1 = "";
					scenemsg2 = "";
					pata_mode = 0;
				}
			}
		}
		// ���ē��K�C�h��`��
		if (disptype == 3) {// ���ē����C�A�E�g
			back_g.drawImage(img_guide, MX1-MIS*6-FUKIW-150, MAPY1-ICONSIZE, 118, 280, this);// �K�C�h�\��
		}
		// ���ē��ӂ�������`��
		if (disptype == 3 && charahuki.length() > 0) {// ���ē����C�A�E�g
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
				while (lbreak.getPosition() < len) {// �e�s�ɂ��ă��[�v����
					layout = lbreak.nextLayout(width);// ���̍s�ɕ\�����镶�����Layout����肷��
					y += layout.getAscent() + layout.getDescent();// �\���ꏊ���P�s������
				}

				int poly_x[] = {MX1-MIS*6-FUKIW-40, MX1-MIS*6-FUKIW+1, MX1-MIS*6-FUKIW+1};
				int poly_y[] = {MAPY1+42, MAPY1+34, MAPY1+14};
				back_g.setColor(hcolor);// �ӂ������J���[
				back_g.fillRoundRect(MX1-MIS*6-FUKIW, MAPY1, FUKIW, (int)y-MAPY1+MIS, MIS*2, MIS*2);// �t�L�_�V�i�p�ێl�p�j�\��
				back_g.setColor(Color.black);// ��
				back_g.drawRoundRect(MX1-MIS*6-FUKIW, MAPY1, FUKIW, (int)y-MAPY1+MIS, MIS*2, MIS*2);// �t�L�_�V�i�p�ێl�p�j�\��
				back_g.setColor(hcolor);// �ӂ������J���[
				back_g.fillPolygon(poly_x, poly_y, 3);// �t�L�_�V�i�O�p�j�\��
				back_g.setColor(Color.black);// ��
				back_g.drawLine(poly_x[0], poly_y[0], poly_x[1], poly_y[1]);// �t�L�_�V�i���j�\��
				back_g.drawLine(poly_x[0], poly_y[0], poly_x[2], poly_y[2]);// �t�L�_�V�i���j�\��

				y = MAPY1+MIS;
				lbreak.setPosition(0);
				while (lbreak.getPosition() < len) {// �e�s�ɂ��ă��[�v����
					layout = lbreak.nextLayout(width);// ���̍s�ɕ\�����镶�����Layout����肷��
					y += layout.getAscent() + layout.getDescent();// �\���ꏊ���P�s������
					layout.draw(back_g, x, y);
				}
			}
			label2text = "";
		}
		if (pata_mode != 2) {// �p�m���}��`��
//System.out.print("["+PanoId.elementAt(point)+","+muki+"]");
			int prw = PANOW*100/panorate;// �\���T�C�Y�ɕ\���\�ȃp�m���}�T�C�Y
			int prh = PANOH*100/panorate;// �\���T�C�Y�ɕ\���\�ȃp�m���}�T�C�Y
			int pnw = pano_w*panorate/100;// �K�v�ȕ\���T�C�Y
			int pnh = pano_h*panorate/100;// �K�v�ȕ\���T�C�Y
			int pnm = muki*panorate/100;// �K�v�ȕ\���T�C�Y(0�`muki)
			int py1 = Math.max(PANOY1, PANOY1+(PANOH-pnh)/2);// �\���ʒu���P
			int py2 = Math.min(PANOY1+PANOH, py1 + pnh);// �\���ʒu���Q
			int qy1 = Math.max(0, (pano_h-prh)/2);// �\���p�m���}���P
			int qy2 = Math.min(pano_h, qy1 + prh);// �\���p�m���}���Q
			if (muki < prw/2) {
				back_g.drawImage(Pano[point].img, PX1, py1, PMX-pnm, py2, pano_w+muki-prw/2, qy1, pano_w, qy2, this);// �s�����摜�\��
				back_g.drawImage(Pano[point].img, PMX-pnm, py1, PX2, py2, 0, qy1, muki+prw/2, qy2, this);// �s�����摜�\��
			} else if (muki < pano_w-prw/2) {
				back_g.drawImage(Pano[point].img, PX1, py1, PX2, py2, muki-prw/2, qy1, muki+prw/2, qy2, this);// �s�����摜�\��
			} else if (muki < pano_w) {
				back_g.drawImage(Pano[point].img, PX1, py1, PMX+pnw-pnm, py2, muki-prw/2, qy1, pano_w, qy2, this);// �s�����摜�\��
				back_g.drawImage(Pano[point].img, PMX+pnw-pnm, py1, PX2, py2, 0, qy1, muki-pano_w+prw/2, qy2, this);// �s�����摜�\��
			}
			back_g.setStroke(stroke2);
			for (int i=0; i<links[point].linkSet.area.length; i++){// �g�\�����[�v
				LinkArea la = links[point].linkSet.area[i];
				int dx1 = dispX(pano_w, la.sx);
				int dx2 = dispX(pano_w, la.ex);
				int dy1 = dispY(pano_h, la.sy);
				int dy2 = dispY(pano_h, la.ey);
				if (dy1 == -1 && dy2 >= PANOY1) dy1 = PANOY1;
				if (dy2 == PANOY1+PANOH+1 && dy1 < PANOY1+PANOH) dy2 = PANOY1+PANOH-1;
				if (waku_mode == 0 && i != waku_no) {// �g��\���H
					dy1 = py2 - 2;
					dy2 = py2 - 1;
				}
				if ((dx1 >= 0 || dx2 >= 0) && (dy1 >= PANOY1 && dy1 <= PANOY1+PANOH) && (dy2 >= PANOY1 && dy2 <= PANOY1+PANOH+1)) {// �\���͈͓�
					if (dx1 < 0) dx1 = PX1;
					if (dx2 < 0) dx2 = PX2-1;
					if (task1 == null && pata_mode != 3 && la.id >= 0 && la.id <= LINKIDMIN-1) {// �p�^�p�^�ւ̃����N
						back_g.setColor(Color.red);// ��
						back_g.drawRect(dx1, dy1, dx2-dx1, dy2-dy1); // �l�p
					} else if (la.id >= LINKIDMIN && la.id <= WARPIDMIN-1) {// �I�u�W�F�N�g�ւ̃����N
						back_g.setColor(Color.blue);// ��
						back_g.drawRect(dx1, dy1, dx2-dx1, dy2-dy1); // �l�p
					} else if (task1 == null && pata_mode != 3 && la.id >= WARPIDMIN) {// ���[�v
						back_g.setColor(Color.yellow);// ��
						back_g.drawRect(dx1, dy1, dx2-dx1, dy2-dy1); // �l�p
					}
				}
			}
			back_g.setStroke(stroke1);
			if (waku_no >= 0) {// �g�����\���H
				LinkArea la = links[point].linkSet.area[waku_no];
				if (la.id >= 0 && la.id <= LINKIDMIN-1) {// �p�^�p�^�ւ̃����N
					label2text = "���̕����֐i�݂܂��B";
//					label2text = "���̕����֐i�݂܂��B["+PanoId.elementAt(la.id)+"]";
				} else if (la.id >= LINKIDMIN && la.id <= WARPIDMIN-1) {// �I�u�W�F�N�g�ւ̃����N
					label2text = "�֘A�������\�����܂��B";
//					label2text = "�֘A�������\�����܂��B["+LinkObj[la.data1]+"]";
				} else {// ���[�v
					label2text = "�ʂ̏ꏊ�փW�����v���܂��B";
//					label2text = "�ʂ̏ꏊ�փW�����v���܂��B["+LinkObj[la.data1]+":"+la.data3+"]";
				}
			}
			if (button_no > 0) {// �{�^�������\���H
				label2text = BTNMSG[button_no];
			}
			// �p�m���}�g��`��
			back_g.setColor(wcolor);// ���N�J���[

			back_g.fill3DRect(PX1-ICONSIZE, PANOY1+PANOH, ICONSIZE, ICONSIZE, true); // �p�m���}�����l�p
			back_g.drawImage(img_arwicon, PX1-ICONSIZE, PANOY1+PANOH, PX1, PANOY1+PANOH+ICONSIZE, 0, 60, 16, 76, this);// �k���A�C�R���摜�\��
			back_g.fill3DRect(PX2, PANOY1+PANOH, ICONSIZE, ICONSIZE, true); // �p�m���}�E���l�p
			back_g.drawImage(img_arwicon, PX2, PANOY1+PANOH, PX2+ICONSIZE, PANOY1+PANOH+ICONSIZE, 16, 60, 32, 76, this);// �g��A�C�R���摜�\��

			back_g.fill3DRect(PX1-MIS, PANOY1-MIS, PANOW+MIS*2, MIS, true); // �p�m���}��l�p
			back_g.setClip(PX1-1, PANOY1+PANOH, PANOW+2, MIS);// �N���b�v�̈���w��
			back_g.fill3DRect(PX1-MIS, PANOY1+PANOH, PANOW+MIS*2, MIS, true); // �p�m���}���l�p
			back_g.setClip(PX1-MIS, PANOY1-1, MIS, PANOH+2);// �N���b�v�̈���w��
			back_g.fill3DRect(PX1-MIS, PANOY1-2, MIS, PANOH+4, true); // �p�m���}���l�p
			back_g.setClip(PX2, PANOY1-1, MIS, PANOH+2);// �N���b�v�̈���w��
			back_g.fill3DRect(PX2, PANOY1-2, MIS, PANOH+4, true); // �p�m���}�E�l�p

			back_g.setClip(PX1-10-ARWW1-10-ARWW2-5, PMY-ARWH/2-5, 5+ARWW2+10+ARWW1+1, ARWH+10);// �N���b�v�̈���w��
			back_g.fill3DRect(PX1-10-ARWW1-10-ARWW2-5, PMY-ARWH/2-5, 5+ARWW1+10+ARWW2+2, ARWH+10, true); // �p�m���}���l�p
			back_g.setClip(PX2+10-1, PMY-ARWH/2-5, 1+ARWW1+10+ARWW2+5, ARWH+10);// �N���b�v�̈���w��
			back_g.fill3DRect(PX2+10-2, PMY-ARWH/2-5, 2+ARWW1+10+ARWW2+5, ARWH+10, true); // �p�m���}�E�l�p
			back_g.setClip(null);// �N���b�v�̈������
			if (rota_mode == 1 || rota_mode == 11) {
				back_g.drawImage(img_arwicon, PX1-10-ARWW1, PMY-ARWH/2, PX1-10, PMY+ARWH/2, 0, 30, 40, 60, this);// �����摜�\��
			} else {
				back_g.drawImage(img_arwicon, PX1-10-ARWW1, PMY-ARWH/2, PX1-10, PMY+ARWH/2, 0, 0, 40, 30, this);// �����摜�\��
			}
			if (rota_mode == 2 || rota_mode == 12) {
				back_g.drawImage(img_arwicon, PX2+10, PMY-ARWH/2, PX2+10+ARWW1, PMY+ARWH/2, 40, 30, 80, 60, this);// �E���摜�\��
			} else {
				back_g.drawImage(img_arwicon, PX2+10, PMY-ARWH/2, PX2+10+ARWW1, PMY+ARWH/2, 40, 0, 80, 30, this);// �E���摜�\��
			}
			if (rota_mode == 3 || rota_mode == 13) {
				back_g.drawImage(img_arwicon, PX1-10-ARWW1-10-ARWW2, PMY-ARWH/2, PX1-10-ARWW1-10, PMY+ARWH/2, 0, 30, 40, 60, this);// �����摜�\��
			} else {
				back_g.drawImage(img_arwicon, PX1-10-ARWW1-10-ARWW2, PMY-ARWH/2, PX1-10-ARWW1-10, PMY+ARWH/2, 0, 0, 40, 30, this);// �����摜�\��
			}
			if (rota_mode == 4 || rota_mode == 14) {
				back_g.drawImage(img_arwicon, PX2+10+ARWW1+10, PMY-ARWH/2, PX2+10+ARWW1+10+ARWW2, PMY+ARWH/2, 40, 30, 80, 60, this);// �E���摜�\��
			} else {
				back_g.drawImage(img_arwicon, PX2+10+ARWW1+10, PMY-ARWH/2, PX2+10+ARWW1+10+ARWW2, PMY+ARWH/2, 40, 0, 80, 30, this);// �E���摜�\��
			}
			label1.setText("["+PanoId.elementAt(point)+","+muki+"]");
		} else {// �p�^�p�^��`��
//System.out.print("("+pata_now+")");
			if (PataImg[pata_now] == null) {// �����[�h�Ȃ烍�[�h����
				PataImg[pata_now] = LoadImage(PataUri.elementAt(pata_now).toString());
			}
			int tw = PataImg[pata_now].getWidth(this);
			int th = PataImg[pata_now].getHeight(this);
			int py1 = Math.max(PANOY1, PMY-th/2);// �\���ʒu���P
			int py2 = Math.min(PANOY1+PANOH, py1 + th);// �\���ʒu���Q
			int qy1 = Math.max(0, (th-PANOH)/2);// �\���p�^�p�^���P
			int qy2 = Math.min(th, qy1 + PANOH);// �\���p�^�p�^���Q
			if (tw >= PANOW) {
				back_g.drawImage(PataImg[pata_now], PX1, py1, PX2, py2, (tw-PANOW)/2, qy1, (tw+PANOW)/2, qy2, this);// �s�����摜�\��
			} else {
				back_g.drawImage(PataImg[pata_now], PMX-tw/2, py1, PMX+tw/2, py2, 0, qy1, tw, qy2, this);// �s�����摜�\��
			}
			label1.setText("("+pata_now+")");
			// �p�m���}�g��`��
			back_g.setColor(wcolor);// ���N�J���[
			back_g.fill3DRect(PX1-MIS, PANOY1-MIS, PANOW+MIS*2, MIS, true); // �p�m���}��l�p
			back_g.fill3DRect(PX1-MIS, PANOY1+PANOH, PANOW+MIS*2, MIS, true); // �p�m���}���l�p
			back_g.setClip(PX1-MIS, PANOY1-1, MIS, PANOH+2);// �N���b�v�̈���w��
			back_g.fill3DRect(PX1-MIS, PANOY1-2, MIS, PANOH+4, true); // �p�m���}���l�p
			back_g.setClip(PX2, PANOY1-1, MIS, PANOH+2);// �N���b�v�̈���w��
			back_g.fill3DRect(PX2, PANOY1-2, MIS, PANOH+4, true); // �p�m���}�E�l�p
			back_g.setClip(null);// �N���b�v�̈������
		}
		label2.setText(label2text);

		}// end of paintPano()

		public void update (Graphics g) {
//			back_g.clearRect(0,0,getSize().width,getSize().height);
			paint(g);
		}
	}

	// �n�}�̕\�����S����̑��Έʒu�������߂�
	public int Calc_ddx(int x, int y) {
		return (int)((Math.cos(mapa*Math.PI/180.0)*(x-mapx)-Math.sin(mapa*Math.PI/180.0)*(y-mapy))*maprate/100);
	}

	// �n�}�̕\�����S����̑��Έʒu�������߂�
	public int Calc_ddy(int x, int y) {
		return (int)((Math.sin(mapa*Math.PI/180.0)*(x-mapx)+Math.cos(mapa*Math.PI/180.0)*(y-mapy))*maprate/100);
	}

	// �摜��ǂݍ���
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

	// �e�L�X�g��ǂݍ���
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
		if (Pano[point].img == null) {// �����[�h�Ȃ烍�[�h����
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
		if (rota_mode > 0 && rota_mode <= 10) {//��]���[�h
			rota_mode = rota_mode + 10;
		}
		if (rota_mode > 10) {//��]���[�h
			muki = (muki+d_muki+pano_w) % pano_w;
			canvas1.repaint();
		}
		if (map_mode == 1 || map_mode == 11) {//�n�}���샂�[�h�i�ړ��j
			map_mode = 11;
			mapx = Math.min(Math.max(mapx + d_mapx, MAPW/2*100/maprate), mapw-MAPW/2*100/maprate);
			mapy = Math.min(Math.max(mapy + d_mapy, MAPH/2*100/maprate), maph-MAPH/2*100/maprate);
			canvas1.repaint();
		} else if (map_mode == 2 || map_mode == 12) {//�n�}���샂�[�h�i�g��^�k���j
			map_mode = 12;
			if (d_maprate < 0 && maprate > MAP_ZOOM_MIN || d_maprate > 0 && maprate < MAP_ZOOM_MAX) {
				maprate = maprate + (maprate+1)/d_maprate;
				mpps = MPPS - Math.max(0, (100-maprate)/25);
				mois = MOIS - Math.max(0, (100-maprate)/5);
				fitMap2();
				canvas1.repaint();
			}
		} else if (map_mode == 3 || map_mode == 13) {//�n�}���샂�[�h�i��]�j
			map_mode = 13;
			mapa = (mapa+d_mapa) % 360;
			canvas1.repaint();
		} else if (map_mode == 4 || map_mode == 14) {//�p�m���}���샂�[�h�i�g��^�k���j
			map_mode = 14;
			if (d_panorate < 0 && panorate > PANO_ZOOM_MIN || d_panorate > 0 && panorate < PANO_ZOOM_MAX) {
				panorate = panorate + (panorate+1)/d_panorate;
				canvas1.repaint();
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object == btn1) { // �{�^���N���b�N�C�x���g
			ReadData();
			canvas1.repaint();
		} else if (object == btn2) { // �{�^���i�����ʒu�j�N���b�N�C�x���g
			PanoInit(initpoint);
			muki = initmuki;
			canvas1.repaint();
		} else if (object == SRBtn) { // �{�^���i�V�i���I�Ǎ��j�N���b�N�C�x���g
			ReadScenario();
			canvas1.repaint();
		} else if (object == SSBtn) { // �{�^���i�V�i���I�J�n�^�ĊJ�^�ꎞ��~�j�N���b�N�C�x���g
			if (SSBtn.getLabel().equals(SSBTNLABEL1)) {// �V�i���I�J�n
				action_SSBtn_1();
			} else if (SSBtn.getLabel().equals(SSBTNLABEL2)) {// �ĊJ
				if (pata_mode == 0 || pata_mode == 4 || pata_mode == 5 || pata_mode == 6) {
					action_SSBtn_1();
				} else {
					tm1start();
					SSBtn.setLabel(SSBTNLABEL3);
					SCBtn.setLabel(SCBTNLABEL3);
				}
			} else if (pata_mode != 0 && SSBtn.getLabel().equals(SSBTNLABEL3)) {// �ꎞ��~
				tm1stop();
				SSBtn.setLabel(SSBTNLABEL2);
				SCBtn.setLabel(SCBTNLABEL2);
				canvas1.repaint();
			}
		} else if (object == SCBtn) { // �{�^���i�R�}����^���~�j�N���b�N�C�x���g
			if (SCBtn.getLabel().equals(SCBTNLABEL3)) {// ���~
				scenemsg1 = "";
				scenemsg2 = "";
				pata_mode = 0;
				tm1stop();
				scene = scene.delete(0,scene.length());
				SSBtn.setLabel(SSBTNLABEL1);
				SCBtn.setLabel(SCBTNLABEL1);
				canvas1.repaint();
			} else if (SCBtn.getLabel().equals(SCBTNLABEL1)) {// �R�}����
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
		if (pata_mode == 1) {// �p�^�p�^�������[�h
			pata_mode = 2;// �p�^�p�^���[�h
			norepaint = 0;
			action_tm1_1();
		} else if (pata_mode == 2) {// �p�^�p�^���[�h
			action_tm1_1();
		} else if (pata_mode == 3) {// �p�m���}�Ŏ�����]
			action_tm1_2();
		}
	}

	public void action_tm1_1() {// �p�^�p�^���[�h
		if (pata_now >= 0 && PataImg[pata_now] != null) {
			PataImg[pata_now].flush();// ���\�[�X���
			PataImg[pata_now] = null;
		}
		if (pata_now < pata_to) {
			pata_now = pata_now + 1;
			canvas1.repaint();
			if (pata_now < pata_to && PataImg[pata_now+1] == null) {// �p�^�p�^�̐�ǂ݂��K�v���H
				PataImg[pata_now+1] = LoadImage(PataUri.elementAt(pata_now+1).toString());
			}
		} else if (scene.length()==0) {// �V�i���I�Ȃ�
			pata_mode = 6;// �I���������[�h�i�P�񂾂������\�����Apata_mode = 0 �ƂȂ�j
			tm1stop();
			SSBtn.setLabel(SSBTNLABEL1);
			SCBtn.setLabel(SCBTNLABEL1);
			canvas1.repaint();
		} else if (task1 != null && SSBtn.getLabel().equals(SSBTNLABEL2)) {
			pata_mode = 4;// �ꎞ��~�������[�h
			tm1stop();
			canvas1.repaint();
		} else {// �V�i���I���c���Ă���
//System.out.print("<"+scene.toString()+">");
			String nextstr = "";
			int nextpid = -1;
			int c = scene.toString().indexOf(';');
			if (c == -1) {// �P�c���Ă���
				nextstr = scene.toString();
				scene = scene.delete(0,scene.length());
			} else {// �����c���Ă���
				nextstr = scene.substring(0,c);
				scene = scene.delete(0,c+1);
			}
			int c1 = nextstr.lastIndexOf(',');
			scenemsg2 = nextstr.substring(c1+1);// �V�i���I���i�����b�Z�[�W
			nextstr = nextstr.substring(0,c1);
			c1 = nextstr.lastIndexOf(',');
			scenemsg1 = nextstr.substring(c1+1);// �V�i���I��]�����b�Z�[�W
			nextstr = nextstr.substring(0,c1);
			if (nextstr.equals("stop")) {// �r���ŃX�g�b�v����
				System.out.println("Stop!!");
				pata_mode = 4;// �ꎞ��~�������[�h
				tm1stop();
				SSBtn.setLabel(SSBTNLABEL2);
				SCBtn.setLabel(SCBTNLABEL2);
			} else {
//				nextpid = Integer.parseInt(nextstr);
				nextpid = getId(nextstr);// �h�c�ԍ����擾
				if (nextpid < 0) {// �G���[
					System.out.println("�h�c:" + nextstr + "���s���ł�");
					pata_mode = 0;
					tm1stop();
					SSBtn.setLabel(SSBTNLABEL1);
					SCBtn.setLabel(SCBTNLABEL1);
					canvas1.repaint();
					return;
				}
				pata_index = getAreaIndex(nextpid);// �̈�C���f�b�N�X���擾
				pata_muki = getMuki(pata_index, 0);// �ڕW�������擾
				pata_mode = 3;// ��]���[�h
				if (pata_index < 0) {// �G���[
					pata_mode = 0;
					tm1stop();
					SSBtn.setLabel(SSBTNLABEL1);
					SCBtn.setLabel(SCBTNLABEL1);
				}
			}
			canvas1.repaint();
		}
	}

	public void action_tm1_2() {// �p�m���}�Ŏ�����]
		if (muki == pata_muki) {// �ڕW�����ɒB�����I
			if (links[point].linkSet.area[pata_index].id < LINKIDMIN) {// �p�m���}�h�c
				if (links[point].linkSet.area[pata_index].data2 > 0) {// �p�^�p�^���P���ȏ゠��
					pata_from = links[point].linkSet.area[pata_index].data1;
					pata_now = pata_from;
					pata_to = pata_now + links[point].linkSet.area[pata_index].data2 - 1;
					pata_mode = 2;// �p�^�p�^���[�h
					if (PataImg[pata_now] == null) {// �����[�h�Ȃ烍�[�h����
						PataImg[pata_now] = LoadImage(PataUri.elementAt(pata_now).toString());
					}
				} else {
					pata_mode = 1;// �p�^�p�^�������[�h
				}
				backpoint = point;
				backmuki = muki;
				PanoInit(links[point].linkSet.area[pata_index].id);
				pata_index = getAreaIndex(backpoint);// �̈�C���f�b�N�X���擾
				muki = getMuki(pata_index, 1);// �t�������擾
			} else if (links[point].linkSet.area[pata_index].id >= WARPIDMIN) {// ���[�v�h�c
				norepaint = 1;
				int lo = links[point].linkSet.area[pata_index].data1;
				String data3 = links[point].linkSet.area[pata_index].data3;
				String data4 = links[point].linkSet.area[pata_index].data4;
				choiceMapxml.select(getMapxmlIndex(LinkObj[lo]));
				ReadData();
				if (!data3.equals("")) {// �����h�c����H
					int id = getId(data3);// �h�c�ԍ����擾
					if (id >= 0) {
						PanoInit(id);
						backpoint = point;
					} else {
						System.out.println("�����h�c:" + data3 + "���s���ł�");
					}
					if (!data4.equals("")) {// ������������H
						int gyaku = 0;
						if (data4.startsWith("!")) {// �t�����H
							data4 = data4.substring(1);
							gyaku = 1;
						}
						int id2 = getId(data4);// �h�c�ԍ����擾
						if (id2 >= 0) {
							muki = getMuki(getAreaIndex(id2), gyaku);// �������擾����
						} else {
							System.out.println("��������:" + data4 + "���s���ł�");
						}
					}
				}
				pata_now = 0;
				pata_to = 0;
				norepaint = 0;
				pata_mode = 1;// �p�^�p�^�������[�h
			} else {
				pata_mode = 2;// �p�^�p�^���[�h
				action_tm1_1();
			}
		} else {
			int dmuki = (pata_muki-muki+pano_w) % pano_w;
			if (dmuki <= pano_w/2) {// �E���
				if (dmuki < pano_w/24) {
					muki = pata_muki;
				} else {
					muki = (muki + pano_w/24) % pano_w;
				}
			} else {//�����
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
		if (disptype == 2) {// �n�}���C�A�E�g�����ē����C�A�E�g
			disptype = 3;
			initSize();
			initLayout();
			fitMap1();
		}
		if (scene.length() == 0) {// �V�i���I���Ȃ�
			if (choScena.getSelectedIndex() >= 0) {// �V�i���I���I������Ă���
				scene.append((String)Scene.elementAt(choScena.getSelectedIndex()));
			}
		}
		if (scene.length() > 0) {// �V�i���I������
//System.out.print("<"+scene.toString()+">");
			String nextstr = "";
			int nextpid = -1;
			int c = scene.toString().indexOf(';');
			if (c == -1) {// �P�c���Ă���
				nextstr = scene.toString();
				scene = scene.delete(0,scene.length());
			} else {// �����c���Ă���
				nextstr = scene.substring(0,c);
				scene = scene.delete(0,c+1);
			}
			int c1 = nextstr.lastIndexOf(',');
			scenemsg2 = nextstr.substring(c1+1);// �V�i���I���i�����b�Z�[�W
			nextstr = nextstr.substring(0,c1);
			c1 = nextstr.lastIndexOf(',');
			scenemsg1 = nextstr.substring(c1+1);// �V�i���I��]�����b�Z�[�W
			nextstr = nextstr.substring(0,c1);
			if (nextstr.equals("stop")) {// �r���ŃX�g�b�v����
				System.out.println("Stop!!");
				pata_mode = 4;// �ꎞ��~�������[�h
				tm1stop();
				SSBtn.setLabel(SSBTNLABEL1);
				SCBtn.setLabel(SCBTNLABEL1);
			} else {
//				nextpid = Integer.parseInt(nextstr);
				nextpid = getId(nextstr);// �h�c�ԍ����擾
				if (nextpid < 0) {// �G���[
					System.out.println("�h�c:" + nextstr + "���s���ł�");
					return;
				}
				pata_mode = 1;// �p�^�p�^�������[�h
				pata_now = 0;
				pata_to = 0;
				tm1start();// �^�C�}�[�X�^�[�g
				SSBtn.setLabel(SSBTNLABEL3);
				SCBtn.setLabel(SCBTNLABEL3);
				int pw = pano_w;
				int pa = pano_a;
				PanoInit(nextpid);
				muki = (int)(((muki*360+((double)pano_a-pa)*pw)*pano_w/360/pw) + pano_w) % pano_w;// ���������ɂ���
//				canvas1.repaint();
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		Object object = e.getSource();
		if (object == choScena) { // �`���C�X�I���C�x���g
			canvas1.repaint();
		} else if (object == choSpeed) { // �`���C�X�I���C�x���g
			int newspeed = choSpeed.getSelectedIndex();
			if (pata_speed != newspeed) {
				pata_speed = newspeed;
				if (pata_speed == 0) {// �x��
					period1 = 1000;
				} else if (pata_speed == 1) {// ����
					period1 = 500;
				} else if (pata_speed == 2) {// ����
					period1 = 300;
				}
				if (task1 != null) {
					tm1stop();
					tm1start();
				}
			}
		} else if (object == chkWaku) { // �`�F�b�N�{�b�N�X�i�g�\���j�N���b�N�C�x���g
			waku_mode = chkWaku.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox1) { // �`�F�b�N�{�b�N�X�i�ʐ^�\���j�N���b�N�C�x���g
			map_imgmode = checkbox1.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox2) { // �`�F�b�N�{�b�N�X�i���S�Œ�j�N���b�N�C�x���g
			map_centermode = checkbox2.getState() ? 1 : 0;
			if (map_centermode == 0 || map_upmode == 0) {
				checkbox4.setVisible(false);
				map_popmode = 0;
			} else {
				checkbox4.setVisible(true);
				map_popmode = checkbox4.getState() ? 1 : 0;
			}
			canvas1.repaint();
		} else if (object == checkbox3) { // �`�F�b�N�{�b�N�X�i��Œ�j�N���b�N�C�x���g
			map_upmode = checkbox3.getState() ? 1 : 0;
			if (map_centermode == 0 || map_upmode == 0) {
				checkbox4.setVisible(false);
				map_popmode = 0;
			} else {
				checkbox4.setVisible(true);
				map_popmode = checkbox4.getState() ? 1 : 0;
			}
			canvas1.repaint();
		} else if (object == checkbox4) { // �`�F�b�N�{�b�N�X�i�ӂ������j�N���b�N�C�x���g
			map_popmode = checkbox4.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox5) { // �`�F�b�N�{�b�N�X�i�V�i���I�j�N���b�N�C�x���g
			map_scemode = checkbox5.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox6) { // �`�F�b�N�{�b�N�X�i���p�\���j�N���b�N�C�x���g
			map_dispamode = checkbox6.getState() ? 1 : 0;
			canvas1.repaint();
		} else if (object == checkbox7) { // �`�F�b�N�{�b�N�X�i�o�H�\���j�N���b�N�C�x���g
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
		Element element = null;// ���[�N

		String name = "";// name�̒l
		String file = "";// file�̒l
		String key = "";// key�̒l
		String value = "";// value�̒l
		int id = 0;// �h�c
		int i = 0;// ���[�v�p
		int j = 0;// ���[�v�p
		// �n�}�f�[�^����ǂ�
		NodeList mapnodes = rootnode.getElementsByTagName("map");// map�m�[�h����
		int mapnum = mapnodes.getLength();
		for (i = 0; i < mapnum; i++) {// map�������[�v
			Element mapnode = (Element)mapnodes.item(i);// map�m�[�h�擾
			name = getChildNodeValue(mapnode, "name");// name�m�[�h�̒l���i����΁j�擾
			if (name.length() == 0) {
				System.out.println("Initdata read error : No map-name.");
				name = "name"+i;// name�̒l��ݒ�
			}
			choiceMapxml.addItem(name);
			file = getChildNodeValue(mapnode, "file");// file�m�[�h�̒l���i����΁j�擾
			if (file.length() == 0) {
				System.out.println("Initdata read error : No map-file.");
				file = "file"+i;// file�̒l��ݒ�
			}
			Mapxml.addElement(file);
		}
		// �����f�[�^����ǂ�
		NodeList condsnodes = rootnode.getElementsByTagName("conds");// conds�m�[�h����
		int condsnum = Math.min(condsnodes.getLength(), CONDS_MAX);// �������ɐ�������
		for (i = 0; i < condsnum; i++) {// conds�������[�v
			Cond[i] = new Vector();
			Element condsnode = (Element)condsnodes.item(i);// conds�m�[�h�擾
			key = getChildNodeValue(condsnode, "key");// key�m�[�h�̒l���i����΁j�擾
			if (key.length() == 0) {
				System.out.println("Initdata read error : No conds-key.");
				key = "key"+i;// key�̒l��ݒ�
			}
			Condkey[i] = key;
			if (disptype == 0 || disptype == 1) choiceCond[i].setVisible(true);
			NodeList condnodes = condsnode.getElementsByTagName("cond");// cond�m�[�h����
			int condnum = Math.min(condnodes.getLength(), COND_MAX);// �����̍��ڐ��ɐ�������
			for (j = 0; j < condnum; j++) {// cond�������[�v
				Element condnode = (Element)condnodes.item(j);// cond�m�[�h�擾
				name = getChildNodeValue(condnode, "name");// name�m�[�h�̒l���i����΁j�擾
				if (name.length() == 0) {
					System.out.println("Initdata read error : No cond-name.");
					name = "name"+j;// name�̒l��ݒ�
				}
				choiceCond[i].addItem(name);
				value = getChildNodeValue(condnode, "value");// value�m�[�h�̒l���i����΁j�擾
				if (value.length() == 0) {
					System.out.println("Initdata read error : No cond-value.");
					value = "value"+j;// value�̒l��ݒ�
				}
				Cond[i].addElement(value);
			}
		}
		for (; i < CONDS_MAX; i++) {// CONDS_MAX�������[�v
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
		String filename = "scenario.xml";// �V�i���I�w�l�k�t�@�C�������擾
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
		Element element = null;// ���[�N
		Scene = new Vector();
		String name = "";// �V�i���I��
		String mode = "";// ���[�h��
		String msg1 = "";// ���b�Z�[�W�P
		String msg2 = "";// ���b�Z�[�W�Q
		String msg3 = "";// ���b�Z�[�W�R
		String value = "";// ���[�N
		String value2 = "";// ���[�N
		String value3 = "";// ���[�N
		String lastid = "";// ���O�h�c
		int i = 0;// ���[�v�p
		int j = 0;// ���[�v�p
		// �V�i���I����ǂ�
		choScena.removeAll();// �V�i���I�I���{�b�N�X���N���A
		NodeList scenenodes = rootnode.getElementsByTagName("scene");// �V�i���I���m�[�h����
		int scenenum = scenenodes.getLength();
		for (i = 0; i < scenenum; i++) {// �V�i���I��񐔕����[�v
			Element scenenode = (Element)scenenodes.item(i);// �V�i���I���m�[�h�擾
			name = getChildNodeValue(scenenode, "name");// name�m�[�h�̒l���i����΁j�擾
			NodeList posnodes = scenenode.getElementsByTagName("pos");// pos�m�[�h����
			int posnum = posnodes.getLength();
			String scene = "";
			for (j = 0; j < posnum; j++) {// pos�������[�v
				element = (Element)posnodes.item(j);// pos�m�[�h�擾
				mode = element.getAttribute("mode");// mode�����l���擾
				if (element.hasAttribute("msg1")) {
					msg1 = element.getAttribute("msg1");// msg1�����l���擾
				} else {
					msg1 = "";
				}
				if (element.hasAttribute("msg2")) {
					msg2 = element.getAttribute("msg2");// msg2�����l���擾
				} else {
					msg2 = "";
				}
				if (element.hasAttribute("msg3")) {
					msg3 = element.getAttribute("msg3");// msg3�����l���擾
				} else {
					msg3 = "";
				}
				value = element.getFirstChild().getNodeValue();// pos�̒l���擾

				value2 = "";
				value3 = "," + msg1 + "," + msg2;
				int c = value.indexOf(':');
				if (c > 0) {// ":"����H
					value2 = value.substring(c+1);
					value = value.substring(0,c);
					value2 = ";" + value + ":" + value2 + "," + msg3 + ",";
					msg2 = "";
				}
				value3 = value + value3;
				if (!value.equals(lastid)) {// ���O�ƈႤ�h�c
					value2 = ";" + value3 + value2;
				}
				if (j == 0 || mode.equals("start")) {// �ŏ��܂���mode=start
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

	// �摜��ǂݍ���
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
		if (disptype == 2 || disptype == 3) {// �n�}���C�A�E�g�܂��͓��ē����C�A�E�g
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
		String filename = Mapxml.elementAt(choiceMapxml.getSelectedIndex()).toString();// �f�[�^�t�@�C�������擾
		choScena.removeAll();// �V�i���I�I���{�b�N�X���N���A
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
		// �p�m���}�����i�[
		Pano = new Panorama[PanoUri.size()];
		for(int i=0;i<PanoUri.size();i++){
//			Image img = LoadImage(PanoUri.elementAt(i).toString());
			int mx = Integer.parseInt(PanoMx.elementAt(i).toString());
			int my = Integer.parseInt(PanoMy.elementAt(i).toString());
			int angle = Integer.parseInt(PanoAngle.elementAt(i).toString());
			Pano[i] = new Panorama(i, null, mx, my, angle, 0);
		}
		// �p�^�p�^�����i�[
		PataImg = new Image[PataUri.size()];
		for(int i=0;i<PataUri.size();i++){
			PataImg[i] = null;
//			PataImg[i] = LoadImage(PataUri.elementAt(i).toString());
		}
		// �����N�I�u�W�F�N�g���i�[
		LinkObj = new String[LinkUri.size()];
		for(int i=0;i<LinkUri.size();i++){
			LinkObj[i] = LinkUri.elementAt(i).toString();
		}
		// �摜��ǂݍ���
		LoadMapfile();
//		img_map = LoadImage(map_filename);
		mapw = img_map.getWidth(this);
		maph = img_map.getHeight(this);

//		mt.waitForAll();

		// �ӂ������o�n�h���i�[
		Fpoi = new MapObj[FpoiMx.size()];
		for(int i=0;i<FpoiMx.size();i++){
			int mx = Integer.parseInt(FpoiMx.elementAt(i).toString());
			int my = Integer.parseInt(FpoiMy.elementAt(i).toString());
//			Image icon = LoadImage(FpoiIcon.elementAt(i).toString());
//			Image img = LoadImage(FpoiImg.elementAt(i).toString());
//			String txt = LoadText(FpoiTxt.elementAt(i).toString());
			Fpoi[i] = new MapObj(i, mx, my, null, null, null);
		}

		// ���O�o�n�h���i�[
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
		if (!filename.equals(mapxmlfilename)) {// �}�b�v�w�l�k���ύX���ꂽ
			initpoint = 0;
			if (!initidstring.equals("")) {// initid������
				int id = getId(initidstring);// �h�c�ԍ����擾
				if (id >= 0) {
					initpoint = id;
				} else {
					System.out.println("�����h�c:" + initidstring + "���s���ł�");
				}
			}
			PanoInit(initpoint);
			backpoint = point;
			mapx = Pano[point].mx;
			mapy = Pano[point].my;
			fitMap2();

			initmuki = 120;
			if (!initmukistring.equals("")) {// initmuki������
				int gyaku = 0;
				String initmukiid = initmukistring;
				if (initmukiid.startsWith("!")) {// �t�����H
					initmukiid = initmukiid.substring(1);
					gyaku = 1;
				}
				int id2 = getId(initmukiid);// �h�c�ԍ����擾
				if (id2 < 0) {
					id2 = getId(initidstring + ":" + initmukiid);// �h�c�ԍ����擾
				}
				if (id2 >= 0) {
					initmuki = getMuki(getAreaIndex(id2), gyaku);// �������擾����
				} else {
					System.out.println("��������:" + initmukiid + "���s���ł�");
				}
			}
			muki = initmuki;
			backmuki = muki;
		} else {// �}�b�v�w�l�k���ύX����Ȃ�����
			int pw = pano_w;
			int pa = pano_a;
			PanoInit(point);
			backpoint = point;
			muki = (int)(((muki*360+((double)pano_a-pa)*pw)*pano_w/360/pw) + pano_w) % pano_w;// ���������ɂ���
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
		Element element = null;// ���[�N

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
		String name = "";// �V�i���I��
		String value = "";// ���[�N
		String dir = "";
		String pre = "";
		String area = "";
		int type = 0;// Link�̏ꍇ(1:�e�L�X�g,2:�摜,3:����,4:HTML)
		int id = 0;// �h�c
		int c = 0;// �J���}�ʒu
		int i = 0;// ���[�v�p
		int j = 0;// ���[�v�p
		int k = 0;// ���[�v�p
		// �p�m���}����ǂށi�܂��h�c��ݒ�j
		NodeList panonodes = rootnode.getElementsByTagName("pano");// �p�m���}�m�[�h����
		int panonum = panonodes.getLength();
		PanoId = new Vector();
		for (i = 0; i < panonum; i++) {// �p�m���}�������[�v
			Element panonode = (Element)panonodes.item(i);// �p�m���}�m�[�h�擾
			value = panonode.getAttribute("id");// �h�c���擾
			PanoId.addElement(value);// �h�c��ݒ�
		}
		// �p�m���}����ǂ�
		for (i = 0; i < panonum; i++) {// �p�m���}�������[�v
			Vector PanoPoint = new Vector();
			Element panonode = (Element)panonodes.item(i);// �p�m���}�m�[�h�擾
			NodeList mapnodes = panonode.getElementsByTagName("map");// map�m�[�h����
			int mapindex = 0;
			for (j = 0; j < mapnodes.getLength(); j++) {// map�������[�v
				element = (Element)mapnodes.item(j);// map�m�[�h�擾
				if (element.hasAttribute("map01")) {// �����Ɉ�v�����H
					mapindex = j;
					break;
				}
			}
			Element mapnode = (Element)mapnodes.item(mapindex);// map�m�[�h�擾
			value = getChildNodeValue(mapnode, "pos");// pos�m�[�h�̒l���i����΁j�擾
			c = value.indexOf(',');
			if (c < 0) {
				System.out.println("�p�m���}:" + PanoId.elementAt(i).toString() + "����pos�̋L�q�Ɍ�肪����܂�");
			}
			PanoMx.addElement(value.substring(0,c));
			PanoMy.addElement(value.substring(c+1));
			NodeList photonodes = panonode.getElementsByTagName("photo");// photo�m�[�h����
			int photoindex = 0;
			double maxpoint = 0;
			double point = 0;
			for (j = 0; j < photonodes.getLength(); j++) {// photo�������[�v
				element = (Element)photonodes.item(j);// photo�m�[�h�擾
				point = pointCond(element);// ���������ē��_�v�Z
				if (point > maxpoint) {// �ō����_��������
					photoindex = j;
					maxpoint = point;
				}
			}
			Element photonode = (Element)photonodes.item(photoindex);// photo�m�[�h�擾

			value = getChildNodeValue(photonode, "panomuki");// panomuki�m�[�h�̒l���i����΁j�擾
			if (value.length() == 0) {// panomuki�̒l���Ȃ���"0"��ݒ�
				value = "0";
			}
			PanoAngle.addElement(value);

			dir = getChildNodeValue(photonode, "dir");// dir�m�[�h�̒l���i����΁j�擾
			if (dir.length() > 0) {// dir�̒l�����遨"\\"��ǉ�����
				dir = dir + "\\";
			}
			value = getChildNodeValue(photonode, "file");// file�m�[�h�̒l���i����΁j�擾
			PanoUri.addElement(dir + value);

			NodeList patanodes = photonode.getElementsByTagName("pata");// �p�^�p�^�m�[�h����
			NodeList linknodes = photonode.getElementsByTagName("link");// �����N�m�[�h����
			NodeList warpnodes = photonode.getElementsByTagName("warp");// ���[�v�m�[�h����

			for (j = 0; j < patanodes.getLength(); j++) {// �p�^�p�^�������[�v
				Element patanode = (Element)patanodes.item(j);// �p�^�p�^�m�[�h�擾
				value = patanode.getAttribute("toid");// toid�̒l���擾
				id = getId(value);// �h�c�ԍ����擾
				if (id == -1) {
					System.out.println("�p�m���}:" + PanoId.elementAt(i).toString() + "���̂h�c:" + value + "���s���ł�");
					continue;
				}
				area = getChildNodeValue(patanode, "area");// area�m�[�h�̒l���i����΁j�擾
				c = area.indexOf(',');
				if (c < 0) {
					System.out.println("�p�m���}:" + PanoId.elementAt(i).toString() + "����area�̋L�q�Ɍ�肪����܂�");
				}
				int sx = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int sy = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int ex = Integer.parseInt(area.substring(0,c));
				int ey = Integer.parseInt(area.substring(c+1));
				int data1 = PataUri.size();// �p�^�p�^�J�n�ԍ�
				int data2 = 0;// �p�^�p�^��

				NodeList pathnodes = rootnode.getElementsByTagName("path");// path�m�[�h����
				int pathindex = 0;
				for (k = 0; k < pathnodes.getLength(); k++) {// path�������[�v
					element = (Element)pathnodes.item(k);// path�m�[�h�擾
					if (PanoId.elementAt(i).toString().equals(element.getAttribute("fromid")) &&
						value.equals(element.getAttribute("toid"))) {// �h�c����v�����H
						pathindex = k;
						break;
					}
				}
				if (k >= pathnodes.getLength()) {
					System.out.println("path���:"+PanoId.elementAt(i).toString()+"-"+value+"���s���ł�");
					continue;
				}
				Element pathnode = (Element)pathnodes.item(pathindex);// path�m�[�h�擾

				photonodes = pathnode.getElementsByTagName("photo");// photo�m�[�h����
				photoindex = 0;
				maxpoint = 0;
				for (k = 0; k < photonodes.getLength(); k++) {// photo�������[�v
					element = (Element)photonodes.item(k);// photo�m�[�h�擾
					point = pointCond(element);// ���������ē��_�v�Z
					if (point > maxpoint) {// �ō����_��������
						photoindex = k;
						maxpoint = point;
					}
				}
				photonode = (Element)photonodes.item(photoindex);// photo�m�[�h�擾

				int photonum = 0;
				if (photonode.hasAttribute("num")) {// num����������
					photonum = Integer.parseInt(photonode.getAttribute("num"));// photo���擾
				}
				dir = getChildNodeValue(photonode, "dir");// dir�m�[�h�̒l���i����΁j�擾
				if (dir.length() > 0) {// dir�m�[�h�����遨"\\"��ǉ�����
					dir = dir + "\\";
				}

				pre = getChildNodeValue(photonode, "pre");// pre�m�[�h�̒l���i����΁j�擾
				if (pre.length() == 0) {// pre�̒l���Ȃ�
					pre = pathnode.getAttribute("fromid") + "-" + pathnode.getAttribute("toid") + "-";// pre��ݒ肷��i"fromid-toid-"�j
				}
				for (k = 0; k < photonum; k++) {// photo�������[�v
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

			for (j = 0; j < linknodes.getLength(); j++) {// �����N�������[�v
				Element linknode = (Element)linknodes.item(j);// �����N�m�[�h�擾
				id = LINKIDMIN + LinkId.size();// �����N�ԍ�
				value = linknode.getAttribute("id");// �h�c���擾
				LinkId.addElement(PanoId.elementAt(i).toString() + ":" + value);// �h�c��ݒ�
				value = getChildNodeValue(linknode, "object");// object�m�[�h�̒l���i����΁j�擾
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
					System.out.println("���T�|�[�g:"+value);
				}
				area = getChildNodeValue(linknode, "area");// area�m�[�h�̒l���i����΁j�擾
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

			for (j = 0; j < warpnodes.getLength(); j++) {// ���[�v�������[�v
				Element warpnode = (Element)warpnodes.item(j);// ���[�v�m�[�h�擾
				id = WARPIDMIN + WarpId.size();// ���[�v�ԍ�
				value = warpnode.getAttribute("id");// �h�c���擾
				WarpId.addElement(PanoId.elementAt(i).toString() + ":" + value);// �h�c��ݒ�
				value = getChildNodeValue(warpnode, "object");// object�m�[�h�̒l���i����΁j�擾
				if (value.endsWith(".xml")) {
					type = 11;
				} else {
					type = 99;
					System.out.println("���T�|�[�g:"+value);
				}
				area = getChildNodeValue(warpnode, "area");// area�m�[�h�̒l���i����΁j�擾
				c = area.indexOf(',');
				int sx = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int sy = Integer.parseInt(area.substring(0,c));
				area = area.substring(c+1);
				c = area.indexOf(',');
				int ex = Integer.parseInt(area.substring(0,c));
				int ey = Integer.parseInt(area.substring(c+1));
				int data1 = LinkUri.size();// �I�u�W�F�N�g�ԍ�
				LinkUri.addElement(value);

				String data3 = getChildNodeValue(warpnode, "initid");// initid�m�[�h�̒l���i����΁j�擾
				String data4 = getChildNodeValue(warpnode, "initmuki");// initmuki�m�[�h�̒l���i����΁j�擾
				linkArea = new LinkArea(id, sx, sy, ex, ey, data1, type, data3, data4);
				PanoPoint.addElement(linkArea);
			}

			v1.addElement(PanoPoint);
		}
		System.out.println("pano:"+panonum+",pata:"+PataUri.size());
		// �ӂ������o�n�h�^���O�o�n�h��ǂ�
		NodeList pointnodes = rootnode.getElementsByTagName("point");// �o�n�h�m�[�h����
		int pointnum = pointnodes.getLength();
		for (i = 0; i < pointnum; i++) {// �o�n�h�������[�v
			Element pointnode = (Element)pointnodes.item(i);// �o�n�h�m�[�h�擾
			value = pointnode.getAttribute("type");// type���擾
			if (value.equals("2")) {// ���O�o�n�h
				value = getChildNodeValue(pointnode, "pos");// pos�m�[�h�̒l���i����΁j�擾
				c = value.indexOf(',');
				NpoiMx.addElement(value.substring(0,c));
				NpoiMy.addElement(value.substring(c+1));
				value = getChildNodeValue(pointnode, "icon");// icon�m�[�h�̒l���i����΁j�擾
				NpoiIcon.addElement(value);
			} else {// �ӂ������o�n�h
				value = getChildNodeValue(pointnode, "pos");// pos�m�[�h�̒l���i����΁j�擾
				c = value.indexOf(',');
				FpoiMx.addElement(value.substring(0,c));
				FpoiMy.addElement(value.substring(c+1));
				value = getChildNodeValue(pointnode, "icon");// icon�m�[�h�̒l���i����΁j�擾
				FpoiIcon.addElement(value);
				value = getChildNodeValue(pointnode, "img");// img�m�[�h�̒l���i����΁j�擾
				FpoiImg.addElement(value);
				value = getChildNodeValue(pointnode, "txt");// txt�m�[�h�̒l���i����΁j�擾
				FpoiTxt.addElement(value);
			}
		}
		// �V�i���I����ǂ�
		ReadScenarioElement(rootnode);
		// �n�}����ǂ�
		NodeList mapinfonodes = rootnode.getElementsByTagName("mapinfo");// �n�}���m�[�h����
		if (mapinfonodes.getLength() > 0) {
			Element mapinfonode = (Element)mapinfonodes.item(0);// �n�}���m�[�h�擾
			map_filename = getChildNodeValue(mapinfonode, "mapimg");// mapimg�m�[�h�̒l���i����΁j�擾
			initidstring = getChildNodeValue(mapinfonode, "initid");// initid�m�[�h�̒l���i����΁j�擾
			initmukistring = getChildNodeValue(mapinfonode, "initmuki");// initmuki�m�[�h�̒l���i����΁j�擾
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

	// �q�m�[�h�̒l���i����΁j�擾����
	public String getChildNodeValue(Element node, String childnodename) {
	try{
		String value = "";
		NodeList childnodes = node.getElementsByTagName(childnodename);// �q�m�[�h����
		if (childnodes.getLength() > 0) {// �q�m�[�h������H
			Node childnode = childnodes.item(0);
			if (childnode.hasChildNodes()) {// �e�L�X�g�m�[�h������H
				value = childnode.getFirstChild().getNodeValue();// �q�m�[�h�̒l���擾
			}
		}
		return value;
	} catch (Exception e) {
		e.printStackTrace();
		return "";
	}
	}

	// ���������ē��_�v�Z����
	public double pointCond(Element element) {
		double point = 0;
		String cvalue;// �����l
		String svalue;// �I�������l
		for (int i=0; i<CONDS_MAX; i++) {
			if (Condkey[i]!=null && Condkey[i].length()>0 && element.hasAttribute(Condkey[i])) {// ����������H
				cvalue = element.getAttribute(Condkey[i]);// Condkey[i]�̑����l���擾
				svalue = Cond[i].elementAt(choiceCond[i].getSelectedIndex()).toString();// �I�������l���擾
				if (svalue.equals("default")) {// ����="default"
					point = point + Math.pow(10, CONDS_MAX-1-i) * 9;// 9�|�C���g
				} else if (cvalue.equals(svalue)) {// �������r����v����
					point = point + Math.pow(10, CONDS_MAX-1-i) * 8;// 8�|�C���g
				} else {// ��v���Ȃ�
					for (int j=0; j<Cond[i].size(); j++) {
						if (cvalue.equals(Cond[i].elementAt(j).toString())) {// �������r
							point = point + Math.pow(10, CONDS_MAX-1-i) * (7-j);// 7�`0�|�C���g
							break;
						}
					}
				}
			} else {
				point = point + Math.pow(10, CONDS_MAX-1-i) * 9;// 9�|�C���g
			}
		}
		return point;
	}

	// �����Ɉ�v���邩���`�F�b�N����
	public boolean checkCond(Element element) {
		boolean check = true;
		String value;// ���[�N�p
		for (int i=0; i<CONDS_MAX; i++) {
			if (Condkey[i]!=null && Condkey[i].length()>0 && element.hasAttribute(Condkey[i])) {// ����������H
				value = element.getAttribute(Condkey[i]);// Condkey[i]�̑������擾
				if (!value.equals(Cond[i].elementAt(choiceCond[i].getSelectedIndex()).toString())) {// �������r
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
		value = getParameter("filename");//�p�����[�^filename�̒l���󂯎��
		if (value != null) {
			filename = value;
		}
//		value = getParameter("mapbasey");//�p�����[�^mapbasey�̒l���󂯎��
//		if (value != null) {
//			MAPY1 = Integer.parseInt(mapbasey);
//		}
		value = getParameter("pisize");//�p�����[�^pisize�̒l���󂯎��
		if (value != null) {
			pisize = Integer.parseInt(value);
		}
		value = getParameter("misize");//�p�����[�^misize�̒l���󂯎��
		if (value != null) {
			misize = Integer.parseInt(value);
		}
		value = getParameter("colayout");//�p�����[�^colayout�̒l���󂯎��
		if (value != null) {
			colayout = Integer.parseInt(value);
		}
		value = getParameter("disptype");//�p�����[�^disptype�̒l���󂯎��
		if (value != null) {
			disptype = Integer.parseInt(value);
		}
		value = getParameter("scenariofile");//�p�����[�^scenariofile�̒l���󂯎��
		if (value != null) {
			scenariofile = value;
		} else {
			scenariofile = "";
		}
		value = getParameter("scenariono");//�p�����[�^scenariono�̒l���󂯎��
		if (value != null) {
			scenariono = Integer.parseInt(value);
		} else {
			scenariono = -1;
		}
		value = getParameter("initid");//�p�����[�^initid�̒l���󂯎��
		if (value != null) {
			initidparam = value;
		}
		value = getParameter("initmuki");//�p�����[�^initmuki�̒l���󂯎��
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

		// ���C���ݒ�p�l��
		add(pnMain);
		pnMain.setBackground(pbcolor);
		pnMain.setLayout(null);

		// �n�}
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

		// ����
		for (int i=0; i<CONDS_MAX; i++) {
			choiceCond[i] = new Choice();
			pnMain.add(choiceCond[i]);
			choiceCond[i].setVisible(false);
		}

		// �V�i���I
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

		// �p�m���}�ݒ�p�l��
		add(pnPano);
		pnPano.setBackground(pbcolor);
		pnPano.setLayout(null);

		// �p�m���}�ݒ�
		pnPano.add(choSpeed);
		choSpeed.addItem("���x�F������");
		choSpeed.addItem("���x�F�ӂ�");
		choSpeed.addItem("���x�F�͂₢");
		choSpeed.select(1);
		choSpeed.addItemListener(this);
		pnPano.add(chkWaku);
		chkWaku.setForeground(pfcolor);
		chkWaku.setLabel("�����N�g");
		chkWaku.addItemListener(this);
		chkWaku.setState(true);

		// �n�}�ݒ�p�l��
		add(pnMap1);
		pnMap1.setBackground(pbcolor);
		pnMap1.setLayout(null);

		// �n�}�ݒ�
		pnMap1.add(checkbox1);
		checkbox1.setForeground(pfcolor);
		checkbox1.setLabel("�ʐ^�\��");
		checkbox1.addItemListener(this);
		checkbox1.addMouseListener(new MyMouseAdapter());
		checkbox1.setState(true);
		pnMap1.add(checkbox2);
		checkbox2.setForeground(pfcolor);
		checkbox2.setLabel("���S�Œ�");
		checkbox2.addItemListener(this);
		checkbox2.addMouseListener(new MyMouseAdapter());
		pnMap1.add(checkbox3);
		checkbox3.setForeground(pfcolor);
		checkbox3.setLabel("��Œ�");
		checkbox3.addItemListener(this);
		checkbox3.addMouseListener(new MyMouseAdapter());
		pnMap1.add(checkbox4);
		checkbox4.setForeground(pfcolor);
		checkbox4.setLabel("�ӂ�����");
		checkbox4.addItemListener(this);
		checkbox4.addMouseListener(new MyMouseAdapter());
		pnMap1.add(checkbox5);
		checkbox5.setForeground(pfcolor);
		checkbox5.setLabel("�V�i���I");
		checkbox5.addItemListener(this);
		checkbox5.addMouseListener(new MyMouseAdapter());
		pnMap1.add(checkbox6);
		checkbox6.setForeground(pfcolor);
		checkbox6.setLabel("���p�\��");
		checkbox6.addItemListener(this);
		checkbox6.addMouseListener(new MyMouseAdapter());
		checkbox6.setState(true);
		pnMap1.add(checkbox7);
		checkbox7.setForeground(pfcolor);
		checkbox7.setLabel("�o�H�\��");
		checkbox7.addItemListener(this);
		checkbox7.addMouseListener(new MyMouseAdapter());

		// �_�~�[�p�l��
		add(pnDum1);
		pnDum1.setBackground(pbcolor);
		pnDum1.setLayout(null);

		// ���x��
		add(label1);
		label1.setForeground(fcolor);
		add(label2);
		label2.setForeground(fcolor);

		// �V�i���I�p
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

		// �摜��ǂݍ���
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

	// �ȉ��̒l��ݒ�
	//	static int FRAMEW = 760;			// �t���[����
	//	static int FRAMEH = 640;			// �t���[������
	//	static int CANVASX = 0;				// �L�����o�X�w
	//	static int CANVASY = 25;			// �L�����o�X�x
	//	static int CANVASW = 760;			// �L�����o�X��
	//	static int CANVASH = 580;			// �L�����o�X����
	//	static int PANOY1 = 20;				// �p�m���}�\����ʒu��
	//	static int PANOW = 480;				// �p�m���}�\����
	//	static int PANOH = 240;				// �p�m���}�\������
	//	static int MAPY1 = 320;				// �n�}�\����ʒu��
	//	static int MAPW = 240;				// �n�}�\����
	//	static int MAPH = 240;				// �n�}�\������
	//	static int FUKIW = 160;				// �ӂ������\����
	//	static int FUKIH = 240;				// �ӂ������\������
	//	static int FUKIY1 = 320;			// �ӂ������\����
	//	static int MMX = CANVASW/2;			// �n�}�̒��S��
	//	static int MMY = MAPY1+MAPH/2;		// �n�}�̒��S��
	//	static int MX1 = MMX-MAPW/2;		// �n�}�̍��ʒu
	//	static int MX2 = MMX+MAPW/2;		// �n�}�̉E�ʒu
	//	static int PMX = CANVASW/2;			// �p�m���}�̒��S��
	//	static int PMY = PANOY1+PANOH/2;	// �p�m���}�̒��S��
	//	static int PX1 = PMX-PANOW/2;		// �p�m���}�̍��ʒu
	//	static int PX2 = PMX+PANOW/2;		// �p�m���}�̉E�ʒu
	private void initSize() {
		if (pisize < 240 || pisize > 320) return;
		if (misize < 240 || misize > 320) return;
		FUKIW = misize*2/3;// �ӂ������\����
		FUKIH = misize;// �ӂ������\������
//		CANVASW = Math.max(pisize*2 + MIS*8 + ARWW1*2 + ARWW2*2, misize + FUKIW*2 + MIS*8);
		CANVASW = 856;
		CANVASH = Math.max(610, MIS*2 + pisize + BTNY + MIS*4 + misize + ICONSIZE + MIS);
		if (colayout == 0) {// �{�^���ヌ�C�A�E�g
			CANVASX = SPACEX;
			CANVASY = SPACEY + 78;
			FRAMEH = CANVASY + CANVASH + 5;
			FRAMEW = CANVASX + CANVASW + 5;
		} else if (colayout == 1) {// �{�^�������C�A�E�g
			CANVASX = SPACEX;
			CANVASY = SPACEY;
			FRAMEH = CANVASY + CANVASH + 78 + 5;
			FRAMEW = CANVASX + CANVASW + 5;
		} else if (colayout == 2) {// �{�^�������C�A�E�g
			CANVASX = SPACEX + 140;
			CANVASY = SPACEY;
			FRAMEH = CANVASY + CANVASH + 5;
			FRAMEW = CANVASX + CANVASW + 5;
		} else {// �{�^���E���C�A�E�g
			CANVASX = SPACEX;
			CANVASY = SPACEY;
			FRAMEH = CANVASY + CANVASH + 5;
			FRAMEW = CANVASX + CANVASW + 140 + 5;
		}
		if (disptype == 2) {// �n�}���C�A�E�g
			PANOY1 = 0;
			PANOW = 0;
			PANOH = 0;
			MAPY1 = MIS*2;
			MAPW = pisize + MIS*2 + misize;
			MAPH = pisize + MIS*2 + misize;
			MMX = CANVASW/2+(FUKIW+MIS)/2;// �n�}�̒��S��
			MMY = MAPY1+MAPH/2;// �n�}�̒��S��
			MX1 = MMX-MAPW/2;// �n�}�̍��ʒu
			MX2 = MMX+MAPW/2;// �n�}�̉E�ʒu
			PMX = 0;// �p�m���}�̒��S��
			PMY = 0;// �p�m���}�̒��S��
			PX1 = 0;// �p�m���}�̍��ʒu
			PX2 = 0;// �p�m���}�̉E�ʒu
			FUKIW = Math.min(FUKIW, MX1-MIS*3);// �ӂ������\����
		} else if (disptype == 3) {// ���ē����C�A�E�g
			PANOY1 = MIS*2;
			PANOW = pisize*2;
			PANOH = pisize;
			MAPY1 = PANOY1 + PANOH + BTNY + MIS*4;
			MAPW = misize;
			MAPH = misize;
			MMX = CANVASW/2+MAPW/3+MAPW/2;// �n�}�̒��S��
			MMY = MAPY1+MAPH/2;// �n�}�̒��S��
			MX1 = MMX-MAPW/2;// �n�}�̍��ʒu
			MX2 = MMX+MAPW/2;// �n�}�̉E�ʒu
			PMX = CANVASW/2;// �p�m���}�̒��S��
			PMY = PANOY1+PANOH/2;// �p�m���}�̒��S��
			PX1 = PMX-PANOW/2;// �p�m���}�̍��ʒu
			PX2 = PMX+PANOW/2;// �p�m���}�̉E�ʒu
		} else {// �W�����C�A�E�g
			PANOY1 = MIS*2;
			PANOW = pisize*2;
			PANOH = pisize;
			MAPY1 = PANOY1 + PANOH + BTNY + MIS*4;
			MAPW = misize;
			MAPH = misize;
			MMX = CANVASW/2;// �n�}�̒��S��
			MMY = MAPY1+MAPH/2;// �n�}�̒��S��
			MX1 = MMX-MAPW/2;// �n�}�̍��ʒu
			MX2 = MMX+MAPW/2;// �n�}�̉E�ʒu
			PMX = CANVASW/2;// �p�m���}�̒��S��
			PMY = PANOY1+PANOH/2;// �p�m���}�̒��S��
			PX1 = PMX-PANOW/2;// �p�m���}�̍��ʒu
			PX2 = PMX+PANOW/2;// �p�m���}�̉E�ʒu
		}
		FUKIY1 = MAPY1 + MAPH - FUKIH;// �ӂ������\����
	}

	private void initLayout() {
		setBounds(0,0,FRAMEW,FRAMEH);
		label1.setBounds(CANVASX+PX2+ICONSIZE,CANVASY+PANOY1,100,22);
		label2.setBounds(CANVASX+PX1,CANVASY+PANOY1+PANOH+ICONSIZE,PANOW,22);
		textArea1.setBounds(CANVASX+MX1-MIS*2-FUKIW+MIS,CANVASY+FUKIY1+FUKIW,FUKIW-MIS*2,FUKIH-FUKIW-MIS);
		textArea2.setBounds(CANVASX+MX2+MIS*3,CANVASY+FUKIY1+FUKIW,FUKIW-MIS*2,FUKIH-FUKIW-MIS);
		canvas1.setBounds(CANVASX,CANVASY,CANVASW,CANVASH);
		if (disptype == 0) {// �W�����C�A�E�g
			tfMapxml.setVisible(false);
			choiceMapxml.setVisible(true);
			btn1.setVisible(true);
			btn2.setVisible(true);
			for (int i = 0; i < CONDS_MAX; i++) {// CONDS_MAX�������[�v
				if (choiceCond[i].getItemCount() > 0) {
					choiceCond[i].setVisible(true);
				}
			}
			SRBtn.setVisible(true);
			SSBtn.setVisible(true);
			SCBtn.setVisible(true);
			SSBTNLABEL1 = "�V�i���I�J�n";
			SCBTNLABEL3 = "�V�i���I���~";
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
			if (colayout == 0 || colayout == 1) {// �{�^����܂��͉����C�A�E�g
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
			} else {// �{�^�����܂��͉E���C�A�E�g
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
			if (colayout == 0) {// �{�^���ヌ�C�A�E�g
				panelLayoutTB(SPACEY, 78, 140, 366, 101, 220);// �p�l�����C�A�E�g�ݒ�
			} else if (colayout == 1) {// �{�^�������C�A�E�g
				panelLayoutTB(CANVASY+CANVASH, 78, 140, 366, 101, 220);// �p�l�����C�A�E�g�ݒ�
			} else if (colayout == 2) {// �{�^�������C�A�E�g
				panelLayoutLR(SPACEX, 140, 92, 253, 78, 178);// �p�l�����C�A�E�g�ݒ�
			} else {// �{�^���E���C�A�E�g
				panelLayoutLR(CANVASX+CANVASW, 140, 92, 253, 78, 178);// �p�l�����C�A�E�g�ݒ�
			}
		} else if (disptype == 1) {// �g�o���C�A�E�g
			tfMapxml.setVisible(false);
			choiceMapxml.setVisible(true);
			btn1.setVisible(true);
			btn2.setVisible(true);
			for (int i = 0; i < CONDS_MAX; i++) {// CONDS_MAX�������[�v
				if (choiceCond[i].getItemCount() > 0) {
					choiceCond[i].setVisible(true);
				}
			}
			SRBtn.setVisible(false);
			SSBtn.setVisible(true);
			SCBtn.setVisible(true);
			SSBTNLABEL1 = "�ړ�";
			SCBTNLABEL3 = "�ړ����~";
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
			if (colayout == 0 || colayout == 1) {// �{�^����܂��͉����C�A�E�g
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
			} else {// �{�^�����܂��͉E���C�A�E�g
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
			if (colayout == 0) {// �{�^���ヌ�C�A�E�g
				panelLayoutTB(SPACEY, 78, 140, 366, 101, 220);// �p�l�����C�A�E�g�ݒ�
			} else if (colayout == 1) {// �{�^�������C�A�E�g
				panelLayoutTB(CANVASY+CANVASH, 78, 140, 366, 101, 220);// �p�l�����C�A�E�g�ݒ�
			} else if (colayout == 2) {// �{�^�������C�A�E�g
				panelLayoutLR(SPACEX, 140, 92, 253, 78, 178);// �p�l�����C�A�E�g�ݒ�
			} else {// �{�^���E���C�A�E�g
				panelLayoutLR(CANVASX+CANVASW, 140, 92, 253, 78, 178);// �p�l�����C�A�E�g�ݒ�
			}
		} else if (disptype == 2) {// �n�}���C�A�E�g
			tfMapxml.setVisible(true);
			choiceMapxml.setVisible(false);
			btn1.setVisible(false);
			btn2.setVisible(false);
			for (int i = 0; i < CONDS_MAX; i++) {// CONDS_MAX�������[�v
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
			SSBTNLABEL1 = "���ē��|�J�n";
			SCBTNLABEL3 = "���ē��|���~";
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
			if (colayout == 0 || colayout == 1) {// �{�^����܂��͉����C�A�E�g
				tfMapxml.setBounds(5,5,130,20);
				tfScena.setBounds(5,30,130,20);
				SSBtn.setBounds(140,5,130,44);
				SCBtn.setBounds(275,5,80,44);
				checkbox1.setBounds(147,3,72,22);
				checkbox5.setBounds(3,53,72,22);
			} else {// �{�^�����܂��͉E���C�A�E�g
				tfMapxml.setBounds(5,5,130,20);
				tfScena.setBounds(5,30,130,20);
				SSBtn.setBounds(5,55,130,55);
				SCBtn.setBounds(5,115,130,33);
				checkbox1.setBounds(5,5,72,22);
				checkbox5.setBounds(5,28,72,22);
			}
			if (colayout == 0) {// �{�^���ヌ�C�A�E�g
				panelLayoutTB(SPACEY, 78, 140, 360, 101, 220);// �p�l�����C�A�E�g�ݒ�
			} else if (colayout == 1) {// �{�^�������C�A�E�g
				panelLayoutTB(CANVASY+CANVASH, 78, 140, 360, 101, 220);// �p�l�����C�A�E�g�ݒ�
			} else if (colayout == 2) {// �{�^�������C�A�E�g
				panelLayoutLR(SPACEX, 140, 92, 153, 54, 54);// �p�l�����C�A�E�g�ݒ�
			} else {// �{�^���E���C�A�E�g
				panelLayoutLR(CANVASX+CANVASW, 140, 92, 153, 54, 54);// �p�l�����C�A�E�g�ݒ�
			}
		} else if (disptype == 3) {// ���ē����C�A�E�g
			tfMapxml.setVisible(true);
			choiceMapxml.setVisible(false);
			btn1.setVisible(false);
			btn2.setVisible(false);
			for (int i = 0; i < CONDS_MAX; i++) {// CONDS_MAX�������[�v
				if (choiceCond[i].getItemCount() > 0) {
					choiceCond[i].setVisible(false);
				}
			}
			SRBtn.setVisible(false);
			SSBtn.setVisible(true);
			SCBtn.setVisible(true);
			SSBTNLABEL1 = "���ē��|�J�n";
			SCBTNLABEL3 = "���ē��|���~";
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
			if (colayout == 0 || colayout == 1) {// �{�^����܂��͉����C�A�E�g
				tfMapxml.setBounds(5,5,130,20);
				tfScena.setBounds(5,30,130,20);
				SSBtn.setBounds(140,5,130,44);
				SCBtn.setBounds(275,5,80,44);
				choSpeed.setBounds(3,28,95,22);
				checkbox1.setBounds(147,3,72,22);
			} else {// �{�^�����܂��͉E���C�A�E�g
				tfMapxml.setBounds(5,5,130,20);
				tfScena.setBounds(5,30,130,20);
				SSBtn.setBounds(5,55,130,55);
				SCBtn.setBounds(5,115,130,33);
				choSpeed.setBounds(5,5,95,22);
				checkbox1.setBounds(5,5,72,22);
			}
			if (colayout == 0) {// �{�^���ヌ�C�A�E�g
				panelLayoutTB(SPACEY, 78, 140, 360, 101, 220);// �p�l�����C�A�E�g�ݒ�
			} else if (colayout == 1) {// �{�^�������C�A�E�g
				panelLayoutTB(CANVASY+CANVASH, 78, 140, 360, 101, 220);// �p�l�����C�A�E�g�ݒ�
			} else if (colayout == 2) {// �{�^�������C�A�E�g
				panelLayoutLR(SPACEX, 140, 92, 153, 54, 54);// �p�l�����C�A�E�g�ݒ�
			} else {// �{�^���E���C�A�E�g
				panelLayoutLR(CANVASX+CANVASW, 140, 92, 153, 54, 54);// �p�l�����C�A�E�g�ݒ�
			}
		}
	}

	// �p�l�����C�A�E�g�ݒ�
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

	// �p�l�����C�A�E�g�ݒ�
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
				label2.setText("�o�n�h���ʐ^�ŕ\�����܂��B");
			} else if (object == checkbox2) {
				label2.setText("���ݒn����ɒn�}�̒��S�ɂ��܂��B");
			} else if (object == checkbox3) {
				label2.setText("���ݒn�̌�������ɏ�����ɂ��܂��B");
			} else if (object == checkbox4) {
				label2.setText("�i�s�����ɂo�n�h������ꍇ�A�ӂ�������\�����܂��B");
			} else if (object == checkbox5) {
				label2.setText("�V�i���I���[�g��\�����܂��B");
			} else if (object == checkbox6) {
				label2.setText("���p�\����\�����܂��B");
			} else if (object == checkbox7) {
				label2.setText("�S�Ă̌o�H��\�����܂��B");
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
		if (evx>MX1 && evx<MX2 && evy>MAPY1 && evy<MAPY1+MAPH) {// �n�}���Ƀ}�E�X���������H
			for (int i=0; i<Fpoi.length; i++) {
				ddx = Calc_ddx(Fpoi[i].mx, Fpoi[i].my);// �\�����S����̑��Έʒu
				ddy = Calc_ddy(Fpoi[i].mx, Fpoi[i].my);// �\�����S����̑��Έʒu
				if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �ӂ������o�n�h���n�}�\���͈͓��ɂ���H
					if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// �ӂ������o�n�h�Ƀ}�E�X���������H
						newno = i;
					}
				}
			}
			if (disptype != 2 && (pata_mode == 0 || pata_mode == 4 || pata_mode == 5 || pata_mode == 6)) {// �n�}���C�A�E�g�ȊO���A��~���܂��͈ꎞ��~��
				for (int i=0; i<Pano.length; i++) {
					if (i == point) continue;
					ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
					ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �p�m���}�|�C���g���n�}�\���͈͓��ɂ���H
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// �p�m���}�|�C���g�Ƀ}�E�X���������H
							newcur = 1;
						}
					}
				}
			}
			if (disptype == 2 && map_scemode == 0) {// �n�}���C�A�E�g���A�V�i���I��\��
				for (int i=0; i<Pano.length; i++) {
					ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
					ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �p�m���}�|�C���g���n�}�\���͈͓��ɂ���H
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// �p�m���}�|�C���g�Ƀ}�E�X���������H
							newcur = 1;
						}
					}
				}
			}
		}
		if (pata_mode != 2) {// �p�m���}�摜�\����
			if (evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// �p�m���}�摜���Ƀ}�E�X���������H
				int pw = Pano[point].img.getWidth(this);
				int ph = Pano[point].img.getHeight(this);
//				int py1 = PANOY1+(PANOH-ph)/2;
//				int c = linkCheck(imgX(pw,evx), evy-py1);
				int c = linkCheck(imgX(pw,evx), imgY(ph,evy));
				if (c >= 0) {
					LinkArea la = links[point].linkSet.area[c];
					if (task1 == null && pata_mode != 3 && la.id >= 0 && la.id < Pano.length) {// �p�^�p�^�J�n
						newwakuno = c;
						newcur = 1;
					} else if (la.id >= LINKIDMIN && la.id <= WARPIDMIN-1) {// �����N
						newwakuno = c;
						newcur = 1;
					} else if (task1 == null && pata_mode != 3 && la.id >= WARPIDMIN) {// ���[�v
						newwakuno = c;
						newcur = 1;
					}
				}
			}
			if (evx>PX1-10-ARWW1-10-ARWW2 && evx<PX1-10-ARWW1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {// �����Ƀ}�E�X���������H
				newbuttonno = 1;
				newcur = 1;
			}
			if (evx>PX1-10-ARWW1 && evx<PX1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {// �����Ƀ}�E�X���������H
				newbuttonno = 2;
				newcur = 1;
			}
			if (evx>PX2+10 && evx<PX2+10+ARWW1 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {// �E���Ƀ}�E�X���������H
				newbuttonno = 3;
				newcur = 1;
			}
			if (evx>PX2+10+ARWW1+10 && evx<PX2+10+ARWW1+10+ARWW2 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {// �E���Ƀ}�E�X���������H
				newbuttonno = 4;
				newcur = 1;
			}
			if (evx>PX1-ICONSIZE && evx<PX1 && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {// �k���Ƀ}�E�X���������H
				newbuttonno = 5;
				newcur = 1;
			}
			if (evx>PX2 && evx<PX2+ICONSIZE && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {// �g��Ƀ}�E�X���������H
				newbuttonno = 6;
				newcur = 1;
			}
			if (map_centermode == 0) {// ���S�Œ�łȂ�
				if (evx>MX1-ICONSIZE && evx<MX1 && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//�n�}������
					newbuttonno = 7;
					newcur = 1;
				}
				if (evx>MX2 && evx<MX2+ICONSIZE && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//�n�}�E����
					newbuttonno = 8;
					newcur = 1;
				}
				if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}�㉟��
					newbuttonno = 9;
					newcur = 1;
				}
				if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}������
					newbuttonno = 10;
					newcur = 1;
				}
			}
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}���������i�k���j
				newbuttonno = 11;
				newcur = 1;
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}�E�������i�g��j
				newbuttonno = 12;
				newcur = 1;
			}
			if (map_upmode == 0) {// ��Œ�łȂ�
				if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}���㉟���i����]�j
					newbuttonno = 13;
					newcur = 1;
				}
				if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}�E�㉟���i�E��]�j
					newbuttonno = 14;
					newcur = 1;
				}
			}
		}
		if (fpoi_mode == 1) {// �ӂ������Œ莞�ɂ̓}�E�X���O��Ă��N���A���Ȃ��悤��
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
		if (evx>PX1-10-ARWW1 && evx<PX1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//����󉟉�
			rota_mode = 1;
			d_muki = -PANO_ROTA_UNIT;
			tm2start();// �^�C�}�[�X�^�[�g
			canvas1.repaint();
		}
		if (evx>PX2+10 && evx<PX2+10+ARWW1 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//�E��󉟉�
			rota_mode = 2;
			d_muki = PANO_ROTA_UNIT;
			tm2start();// �^�C�}�[�X�^�[�g
			canvas1.repaint();
		}
		if (evx>PX1-10-ARWW1-10-ARWW2 && evx<PX1-10-ARWW1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//����󉟉�
			rota_mode = 3;
			d_muki = -pano_w/12;
			tm2start();// �^�C�}�[�X�^�[�g
			canvas1.repaint();
		}
		if (evx>PX2+10+ARWW1+10 && evx<PX2+10+ARWW1+10+ARWW2 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//�E��󉟉�
			rota_mode = 4;
			d_muki = pano_w/12;
			tm2start();// �^�C�}�[�X�^�[�g
			canvas1.repaint();
		}
		if (map_centermode == 0) {// ���S�Œ�łȂ�
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//�n�}������
				map_mode = 1;
				d_mapx = (int)(Math.cos((-mapa+180)*Math.PI/180.0)*MAP_MOVE_UNIT);
				d_mapy = (int)(Math.sin((-mapa+180)*Math.PI/180.0)*MAP_MOVE_UNIT);
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//�n�}�E����
				map_mode = 1;
				d_mapx = (int)(Math.cos(-mapa*Math.PI/180.0)*MAP_MOVE_UNIT);
				d_mapy = (int)(Math.sin(-mapa*Math.PI/180.0)*MAP_MOVE_UNIT);
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
			if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}�㉟��
				map_mode = 1;
				d_mapx = (int)(Math.cos((-mapa+270)*Math.PI/180.0)*MAP_MOVE_UNIT);
				d_mapy = (int)(Math.sin((-mapa+270)*Math.PI/180.0)*MAP_MOVE_UNIT);
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
			if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}������
				map_mode = 1;
				d_mapx = (int)(Math.cos((-mapa+90)*Math.PI/180.0)*MAP_MOVE_UNIT);
				d_mapy = (int)(Math.sin((-mapa+90)*Math.PI/180.0)*MAP_MOVE_UNIT);
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
		}
		if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}���������i�k���j
			if (maprate > MAP_ZOOM_MIN) {
				map_mode = 2;
				d_maprate = -MAP_ZOOM_UNIT;
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
		}
		if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}�E�������i�g��j
			if (maprate < MAP_ZOOM_MAX) {
				map_mode = 2;
				d_maprate = MAP_ZOOM_UNIT-1;
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
		}
		if (map_upmode == 0) {// ��Œ�łȂ�
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}���㉟���i����]�j
				map_mode = 3;
				d_mapa = 360-MAP_ROTA_UNIT;
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}�E�㉟���i�E��]�j
				map_mode = 3;
				d_mapa = MAP_ROTA_UNIT;
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
		}
		if (evx>PX1-ICONSIZE && evx<PX1 && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {//�p�m���}�k������
			if (panorate > PANO_ZOOM_MIN) {
				map_mode = 4;
				d_panorate = -PANO_ZOOM_UNIT;
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
		}
		if (evx>PX2 && evx<PX2+ICONSIZE && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {//�p�m���}�g�剟��
			if (panorate < PANO_ZOOM_MAX) {
				map_mode = 4;
				d_panorate = PANO_ZOOM_UNIT-1;
				tm2start();// �^�C�}�[�X�^�[�g
				canvas1.repaint();
			}
		}
		if (evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// �p�m���}�摜��������
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
		if (rota_mode > 10) {// �^�C�}�[�ɂ���]���ꂽ
			rota_mode = 0;
			tm2stop();// �^�C�}�[�X�g�b�v
			repaintflg = true;
		}
		if (rota_mode > 0) {// ��]��
			rota_mode = 0;
			tm2stop();// �^�C�}�[�X�g�b�v
			if (evx>PX1-10-ARWW1 && evx<PX1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//�����N���b�N
				muki = (muki+pano_w-PANO_ROTA_UNIT) % pano_w;
				repaintflg = true;
			}
			if (evx>PX2+10 && evx<PX2+10+ARWW1 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//�E���N���b�N
				muki = (muki+PANO_ROTA_UNIT) % pano_w;
				repaintflg = true;
			}
			if (evx>PX1-10-ARWW1-10-ARWW2 && evx<PX1-10-ARWW1-10 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//�����N���b�N
				muki = (muki+pano_w-pano_w/12) % pano_w;
				repaintflg = true;
			}
			if (evx>PX2+10+ARWW1+10 && evx<PX2+10+ARWW1+10+ARWW2 && evy>PMY-ARWH/2 && evy<PMY+ARWH/2) {//�E���N���b�N
				muki = (muki+pano_w/12) % pano_w;
				repaintflg = true;
			}
		}
		if (map_mode > 10) {// �^�C�}�[�ɂ��n�}���삳�ꂽ
			map_mode = 0;
			tm2stop();// �^�C�}�[�X�g�b�v
		}
		if (map_mode > 0) {// �n�}���쒆
			map_mode = 0;
			tm2stop();// �^�C�}�[�X�g�b�v
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//�n�}���N���b�N
				int dx = (int)(Math.cos((-mapa+180)*Math.PI/180.0)*MAP_MOVE_UNIT);
				int dy = (int)(Math.sin((-mapa+180)*Math.PI/180.0)*MAP_MOVE_UNIT);
				mapx = mapx + dx;
				mapy = mapy + dy;
				fitMap2();
				repaintflg = true;
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MMY-ICONSIZE/2 && evy<MMY+ICONSIZE/2) {//�n�}�E�N���b�N
				int dx = (int)(Math.cos(-mapa*Math.PI/180.0)*MAP_MOVE_UNIT);
				int dy = (int)(Math.sin(-mapa*Math.PI/180.0)*MAP_MOVE_UNIT);
				mapx = mapx + dx;
				mapy = mapy + dy;
				fitMap2();
				repaintflg = true;
			}
			if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}��N���b�N
				int dx = (int)(Math.cos((-mapa+270)*Math.PI/180.0)*MAP_MOVE_UNIT);
				int dy = (int)(Math.sin((-mapa+270)*Math.PI/180.0)*MAP_MOVE_UNIT);
				mapx = mapx + dx;
				mapy = mapy + dy;
				fitMap2();
				repaintflg = true;
			}
			if (evx>MMX-ICONSIZE/2 && evx<MMX+ICONSIZE/2 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}���N���b�N
				int dx = (int)(Math.cos((-mapa+90)*Math.PI/180.0)*MAP_MOVE_UNIT);
				int dy = (int)(Math.sin((-mapa+90)*Math.PI/180.0)*MAP_MOVE_UNIT);
				mapx = mapx + dx;
				mapy = mapy + dy;
				fitMap2();
				repaintflg = true;
			}
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}�����N���b�N�i�k���j
				if (maprate > MAP_ZOOM_MIN) {
					maprate = maprate - (maprate+1)/MAP_ZOOM_UNIT;
					mpps = MPPS - Math.max(0, (100-maprate)/25);
					mois = MOIS - Math.max(0, (100-maprate)/5);
					fitMap2();
					repaintflg = true;
				}
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1+MAPH && evy<MAPY1+MAPH+ICONSIZE) {//�n�}�E���N���b�N�i�g��j
				if (maprate < MAP_ZOOM_MAX) {
					maprate = maprate + (maprate+1)/(MAP_ZOOM_UNIT-1);
					mpps = MPPS - Math.max(0, (100-maprate)/25);
					mois = MOIS - Math.max(0, (100-maprate)/5);
					repaintflg = true;
				}
			}
			if (evx>MX1-ICONSIZE && evx<MX1 && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}����N���b�N�i����]�j
				mapa = (mapa+360-MAP_ROTA_UNIT) % 360;
				repaintflg = true;
			}
			if (evx>MX2 && evx<MX2+ICONSIZE && evy>MAPY1-ICONSIZE && evy<MAPY1) {//�n�}�E��N���b�N�i�E��]�j
				mapa = (mapa+MAP_ROTA_UNIT) % 360;
				repaintflg = true;
			}
			if (evx>PX1-ICONSIZE && evx<PX1 && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {//�p�m���}�k���N���b�N
				if (panorate > PANO_ZOOM_MIN) {
					panorate = panorate - (panorate+1)/PANO_ZOOM_UNIT;
					repaintflg = true;
				}
			}
			if (evx>PX2 && evx<PX2+ICONSIZE && evy>PANOY1+PANOH && evy<PANOY1+PANOH+ICONSIZE) {//�p�m���}�g��N���b�N
				if (panorate < PANO_ZOOM_MAX) {
					panorate = panorate + (panorate+1)/(PANO_ZOOM_UNIT-1);
					repaintflg = true;
				}
			}
		}
		if (fpoi_mode == 1) {// �ӂ������Œ�
			fpoi_mode = 0;// �ӂ������Œ�N���A
			fpoi_no = -1;
			repaintflg = true;
		}
		int drx = MAPW/2;
		int dry = MAPH/2;
		int ddx, ddy;
		if (evx>MX1 && evx<MX2 && evy>MAPY1 && evy<MAPY1+MAPH) {// �n�}�����N���b�N
			if (map_popmode == 0) {// �ӂ��������[�h�ȊO
				for (int i=0; i<Fpoi.length; i++) {
					ddx = Calc_ddx(Fpoi[i].mx, Fpoi[i].my);// �\�����S����̑��Έʒu
					ddy = Calc_ddy(Fpoi[i].mx, Fpoi[i].my);// �\�����S����̑��Έʒu
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �ӂ������o�n�h���n�}�\���͈͓��ɂ���H
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// �ӂ������o�n�h���N���b�N
							fpoi_mode = 1;// �ӂ������Œ�
							fpoi_no = i;
						}
					}
				}
			}
			if (disptype != 2 && (pata_mode == 0 || pata_mode == 4 || pata_mode == 5 || pata_mode == 6)) {// �n�}���C�A�E�g�ȊO���A��~���܂��͈ꎞ��~��
				for (int i=0; i<Pano.length; i++) {
					if (i == point) continue;
					ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
					ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �p�m���}�|�C���g���n�}�\���͈͓��ɂ���H
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// �p�m���}�|�C���g���N���b�N
							int pw = pano_w;
							int pa = pano_a;
							PanoInit(i);
							muki = (int)(((muki*360+((double)pano_a-pa)*pw)*pano_w/360/pw) + pano_w) % pano_w;// ���������ɂ���
							repaintflg = true;
							break;
						}
					}
				}
			}
			if (disptype == 2 && map_scemode == 0) {// �n�}���C�A�E�g���A�V�i���I��\��
				for (int i=0; i<Pano.length; i++) {
					ddx = Calc_ddx(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
					ddy = Calc_ddy(Pano[i].mx, Pano[i].my);// �\�����S����̑��Έʒu
					if (-drx < ddx && ddx < drx && -dry < ddy && ddy < dry) {// �p�m���}�|�C���g���n�}�\���͈͓��ɂ���H
						if (evx>MMX+ddx-mpps && evx<MMX+ddx+mpps && evy>MMY+ddy-mpps && evy<MMY+ddy+mpps) {// �p�m���}�|�C���g���N���b�N
							int pw = pano_w;
							int pa = pano_a;
							PanoInit(i);
							muki = (int)(((muki*360+((double)pano_a-pa)*pw)*pano_w/360/pw) + pano_w) % pano_w;// ���������ɂ���
							maprate = 100;
							mpps = MPPS;
							mois = MOIS;
							mapx = Pano[point].mx;
							mapy = Pano[point].my;
							disptype = 1;// �g�o���C�A�E�g
							initSize();
							initLayout();
							fitMap1();
							repaintflg = true;
							break;
						}
					}
				}
			}
		} else if (pata_mode != 2 && evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// �p�m���}�摜�����N���b�N
			int ph = Pano[point].img.getHeight(this);
//			int py1 = PANOY1+(PANOH-ph)/2;
			int c = linkCheck(imgX(pano_w,evx), imgY(ph,evy));
			if (c >= 0) {
				LinkArea la = links[point].linkSet.area[c];
				if (task1 == null && pata_mode != 3 && la.id >= 0 && la.id < Pano.length) {// �p�^�p�^�J�n
					tm1start();// �^�C�}�[�X�^�[�g
					pata_mode = 1;// �p�^�p�^�������[�h
					norepaint = 1;
					if (la.data2 > 0) {// �p�^�p�^���P���ȏ゠��
						pata_from = la.data1;
						pata_now = pata_from - 1;
						pata_to = pata_now + la.data2;
						if (pata_now < pata_to && PataImg[pata_now+1] == null) {// �p�^�p�^�̐�ǂ݂��K�v���H
							PataImg[pata_now+1] = LoadImage(PataUri.elementAt(pata_now+1).toString());
						}
					}
					backpoint = point;
					backmuki = muki;
					PanoInit(la.id);
					pata_index = getAreaIndex(backpoint);// �̈�C���f�b�N�X���擾
					muki = getMuki(pata_index, 1);// �t�������擾
				} else if (la.id >= LINKIDMIN && la.id <= WARPIDMIN-1) {// �����N
					if (la.data2 == 1) {// �e�L�X�g�\��
						int lo = la.data1;
						textPlayer = new TextPlayer(getCodeBase()+LinkObj[lo], 724);
					} else if (la.data2 == 2) {// �摜�\��
						int lo = la.data1;
						picturePlayer = new PicturePlayer(LoadImage(LinkObj[lo]), 724);
					} else if (la.data2 == 3) {// ����\��
						int lo = la.data1;
//						MovieProductPlayer mpp = new MovieProductPlayer(LinkObj[lo], 650);
					} else if (la.data2 == 4) {// �g�s�l�k�\��
						int lo = la.data1;
						String htmlfile = LinkObj[lo];
						try {// �v������
							if (htmlfile.startsWith("http:")) {
								getAppletContext().showDocument(new URL(htmlfile), "_blank");
							} else {
								getAppletContext().showDocument(new URL(getCodeBase()+htmlfile), "_blank");
							}
						} catch (MalformedURLException e) {  // �G���[����
							System.out.println("URL Error!");
							e.printStackTrace();
						}
					}
				} else if (task1 == null && pata_mode != 3 && la.id >= WARPIDMIN) {// ���[�v
					int lo = la.data1;
					String data3 = la.data3;
					String data4 = la.data4;
					choiceMapxml.select(getMapxmlIndex(LinkObj[lo]));
					ReadData();
					if (!data3.equals("")) {// �����h�c����H
						int id = getId(data3);// �h�c�ԍ����擾
						if (id >= 0) {
							PanoInit(id);
							backpoint = point;
						} else {
							System.out.println("�����h�c:" + data3 + "���s���ł�");
						}
						if (!data4.equals("")) {// ������������H
							int gyaku = 0;
							if (data4.startsWith("!")) {// �t�����H
								data4 = data4.substring(1);
								gyaku = 1;
							}
							int id2 = getId(data4);// �h�c�ԍ����擾
							if (id2 < 0) {
								id2 = getId(data3 + ":" + data4);// �h�c�ԍ����擾
							}
							if (id2 >= 0) {
								muki = getMuki(getAreaIndex(id2), gyaku);// �������擾����
							} else {
								System.out.println("��������:" + data4 + "���s���ł�");
							}
						}
					}
					repaintflg = true;
				}
			}
		} else if (pata_mode == 0 && evx>PX1 && evx<PX2 && evy>PANOY1+PANOH && evy<MAPY1-ICONSIZE) {//���N���b�N
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
			if (evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// �p�m���}�摜�����h���b�O
				if (dragsx > evx) {// ���փh���b�O
					rota_mode = 15;
					d_muki = -1;
					tm2start();// �^�C�}�[�X�^�[�g
				} else if (dragsx < evx) {// �E�փh���b�O
					rota_mode = 15;
					d_muki = 1;
					tm2start();// �^�C�}�[�X�^�[�g
				}
			}
		} else if (rota_mode == 15) {
			if (evx>PX1 && evx<PX2 && evy>PANOY1 && evy<PANOY1+PANOH) {// �p�m���}�摜�����h���b�O
				if (dragsx > evx) {// ���փh���b�O
					d_muki = -1;
					newperiod2 = Math.max(10, DEF_PERIOD2 - (dragsx-evx)/10*10);
					if (dragsx-evx > 50) d_muki = -2;
				} else if (dragsx < evx) {// �E�փh���b�O
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
					tm2stop();// �^�C�}�[�X�g�b�v
					tm2start(0, period2);// �^�C�}�[�X�^�[�g
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

	// �n�}�w�l�k�C���f�b�N�X���擾����
	int getMapxmlIndex(String str) {
		int index = Mapxml.size() - 1;
		for (; index >= 0; index--) {// Mapxml�������[�v
			if (str.equals(Mapxml.elementAt(index).toString())) {// �C���f�b�N�X����v�����H
				break;
			}
		}
		return index;
	}

	// �h�c�ԍ����擾����
	int getId(String str) {
		int id = WarpId.size() - 1;
		for (; id >= 0; id--) {// WarpId�������[�v
			if (str.equals(WarpId.elementAt(id).toString())) {// �h�c����v�����H
				return WARPIDMIN + id;
			}
		}
		id = LinkId.size() - 1;
		for (; id >= 0; id--) {// LinkId�������[�v
			if (str.equals(LinkId.elementAt(id).toString())) {// �h�c����v�����H
				return LINKIDMIN + id;
			}
		}
		id = PanoId.size() - 1;
		for (; id >= 0; id--) {// PanoId�������[�v
			if (str.equals(PanoId.elementAt(id).toString())) {// �h�c����v�����H
				break;
			}
		}
		return id;
	}

	// �̈�C���f�b�N�X���擾����
	int getAreaIndex(int toid) {
		int index = -1;
		for (int i = 0; i < links[point].linkSet.area.length; i++) {
			if (links[point].linkSet.area[i].id == toid) {// to����������
				index = i;
				break;
			}
		}
		if (index < 0) {// �G���[
			System.out.println("Link area error : " + PanoId.elementAt(point) + " - " + PanoId.elementAt(toid));
		}
		return index;
	}

	// �������擾����
	int getMuki(int index, int gyaku) {
		int m = 0;
		if (index >= 0) {
			int sx = links[point].linkSet.area[index].sx;
			int ex = links[point].linkSet.area[index].ex;
			if ((gyaku == 0 && sx <= ex) || (gyaku != 0 && sx > ex)) {// �ʏ����
				m = ((sx + ex) / 2) % pano_w;
			} else {// �t����
				m = ((sx + ex + pano_w) / 2) % pano_w;
			}
		}
		return m;
	}

	// ���W�i���C���j�������N���ɂ��邩���ׂ�
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

	// �摜��̂����\����̂������߂�
	public int dispX(int pw, int x) {
		int dispx = -1;
		int prw = PANOW*100/panorate;// �\���T�C�Y�ɕ\���\�ȃp�m���}�T�C�Y
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

	// �摜��̂����\����̂������߂�
	// �߂�l -1:�����������Ŕ͈͊O�APANOY1+PANOH+1:�傫�������Ŕ͈͊O
	public int dispY(int ph, int y) {
		int dispy = -1;
		int prh = PANOH*100/panorate;// �\���T�C�Y�ɕ\���\�ȃp�m���}�T�C�Y
		int pnh = ph*panorate/100;// �K�v�ȕ\���T�C�Y
		int qy1 = Math.max(0, (ph-prh)/2);// �\���p�m���}���P
		int qy2 = Math.min(ph, qy1 + prh);// �\���p�m���}���Q
		if (y >= qy1 && y < qy2) {
			dispy = PMY-pnh/2 + y*panorate/100;
		} else if (y >= qy2) {
			dispy = PANOY1+PANOH+1;
		}
		return dispy;
	}

	// �\����̂����摜��̂������߂�
	// �߂�l -1:�摜��ɂȂ�
	public int imgX(int pw,int x) {
		int imgx = -1;
		int prw = PANOW*100/panorate;// �\���T�C�Y�ɕ\���\�ȃp�m���}�T�C�Y
		int pnw = pw*panorate/100;// �K�v�ȕ\���T�C�Y
		int pnm = muki*panorate/100;// �K�v�ȕ\���T�C�Y(0�`muki)
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

	// �\����̂����摜��̂������߂�
	// �߂�l -1:�摜��ɂȂ�
	public int imgY(int ph, int y) {
		int imgy = -1;
		int pnh = ph*panorate/100;// �K�v�ȕ\���T�C�Y
		int py1 = Math.max(PANOY1, PMY-pnh/2);// �\���ʒu���P
		int py2 = Math.min(PANOY1+PANOH, py1 + pnh);// �\���ʒu���Q
		if (y >= py1 && y < py2) {
			imgy = ph/2 + (y - PMY)*100/panorate;
		}
		return imgy;
	}

class LinkArea {
	int id;// �p�^�p�^��h�c(0�`LINKIDMIN-1)�^�����N�ԍ�(LINKIDMIN�`WARPIDMIN-1)�^���[�v�ԍ�(WARPIDMIN�`)
	int sx;// �̈捶�゘
	int sy;// �̈捶�゙
	int ex;// �̈�E����
	int ey;// �̈�E����
	int data1;// �p�^�p�^�J�n�ԍ��^�����N�I�u�W�F�N�g�ԍ�
	int data2;// �p�^�p�^���^�����N��^�C�v
	String data3;// �����h�c
	String data4;// ��������
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
