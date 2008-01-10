#!/usr/bin/perl
#  
######################################################################
###
###
###  CGIカウンター TcounT Ver.4.20
###     [1/3] 本体 (tcount.cgi)
###                                 (c) 1996-1999 Takahiro Nishida
###                                 http://www.mytools.net/
###
###
######################################################################
#
### 変数設定部 （詳細は上記ページをご覧下さい） ######################

$basedir = ".";

### 変数設定部 （ここまで）###########################################

require './gifcat.pl';

$lockfile="$basedir/lockdir/tc.lock";
$compfile="$basedir/tccomp.txt";
$logfile="$basedir/tclog.txt";
$timefile="$basedir/tcmtime.txt";


&main;



##### メイン
sub main{
        &lock;
        &check_input;
        &open_counter;
        &increase_counter;
        &show_counter;
        &unlock;
}



##### 入力のチェック
sub check_input{
        $buffer = $ENV{'QUERY_STRING'};
        @prm = split("&", $buffer);
        
        $cname = ($prm[0] =~ /^\w+$/) ? $prm[0] : '';
        $incr  = ($prm[1] == 1)       ? 1       : 0;
        $keta  = ($prm[2] =~ /^\d+$/) ? $prm[2] : 5;
        $type  = ($prm[3] =~ /^\w+$/) ? $prm[3] : 'img';
        $obj   = ($prm[4] =~ /^\w+$/) ? $prm[4] : 'all';
        
        # カウンタ名が指定されていないとエラー
        ($cname) || &error(3);
}



#### カウンターファイルを開く
sub open_counter{
        $cnt = &openfile("$basedir/data/$obj/$cname.txt");
}



##### カウントアップ
sub increase_counter{
        # 増加フラグが立っている場合のみ実行
        return unless ($incr && $obj eq 'all');
        
        # 日付が変わっているかチェック (毎回やると重いのでここで)
        &check_date;
        
        $cnt++;
        &updatefile("$basedir/data/all/$cname.txt", $cnt);
        
        # 今日のカウント数の増加
        #（読み込まれるタイミングによっては、表示がズレること有り）
        local($ctoday) = &openfile("$basedir/data/today/$cname.txt");
        $ctoday++;
        &updatefile("$basedir/data/today/$cname.txt", $ctoday);
}



##### 日付チェック
sub check_date{
        local(@ts);
        @ts = localtime(time);
        $ts[4]++;
        $ts[5] += 1900; # 2000年問題に対応（笑）
        ($ts[4] < 10) && ($ts[4] = "0$ts[4]");
        ($ts[3] < 10) && ($ts[3] = "0$ts[3]");
        $date_now = "$ts[5]$ts[4]$ts[3]";
        $date_old = &openfile($timefile) || "00000000";
        
        # 日付が変わっていたら統計
        ($date_now <= $date_old) || &update_summery;
}



##### カウンター画像の表示
sub show_counter{
        unless(-f "$basedir/gif/$type/1.gif"){
                &error(1, "$basedir/gif/$type/1.gif");
        }
        
        @nos = split("", $cnt);
        while(@nos < $keta){ unshift(@nos, "0"); }
        for(0..$#nos){ $nos[$_]="$basedir/gif/$type/$nos[$_].gif"; }

        print "Content-type: image/gif\n\n";
        binmode(STDOUT);
        $| = 1;
        print &gifcat'gifcat(@nos);
}



##### 日が代わったときの処理
sub update_summery{
        &updatefile($timefile, $date_now); # timefileの書きなおし
        
        return if ($date_old == 0); # 初期値
        return if ($date_old =~ /\//); # 4.10 -> 4.20
        
        # カウンタ名、前日まで、総合、今日のカウント数、比較用バッファ、ログバッファ
        local($cn, $cold, $call, $cday, $compbuf, $logbuf);
        # テンポラリ
        local(@tmps);
        
        # 比較用ファイルのオープン
        open(FILE,"$compfile") || die &error(1, $compfile);
        @tmps = <FILE>;
        close(FILE);
        
        $logbuf  = $date_old;
        $logbuf .= "\t";
        
        foreach(@tmps){
                # ファイル名、前日までのカウント数
                ($cn, $cold) = split("\t");
                # 総合カウント数の取得
                $call = &openfile("$basedir/data/all/$cn.txt");
                # 今日のカウント数の計算（todayは当てにしない）
                $cday = $call - $cold;
                # 比較用ファイルの内容
                $compbuf .= "$cn\t$call\t\n";
                # ログファイルの内容
                $logbuf .= "[$cn]$cday($call)\t";
                # 前日のカウント数の更新
                &updatefile("$basedir/data/day/$cn.txt", $cday);
                # 今日のカウント数のリセット
                &updatefile("$basedir/data/today/$cn.txt", "0");
        }
        
        # ログの追加
        $logbuf .= "\n";
        open(FILE, ">>$logfile") || die &error(2, $logfile);
        print FILE $logbuf;
        close(FILE);
        
        &updatefile($compfile, $compbuf); # 比較用ファイルの更新
}



##### 指定されたカウンターの数値を得る
sub openfile{
        local($cfile) = @_;
        local($cnt);
        open(FILE, "$cfile") || die &error(1, $cfile);
        $cnt = <FILE>;
        close(FILE);
        $cnt;
}



##### 指定されたカウンターに数値を入れる
sub updatefile{
        local($cfile, $cnt) = @_;
        open(FILE, ">$cfile") || die &error(2, $cfile);
        print FILE $cnt;
        close(FILE);
}



##### ファイルロック
sub lock{
        local($try) = 3;
        while(!(symlink("$$",$lockfile))){
                if(--$try <= 0){
                        &error(0);
                }
        sleep(1);
        }
}



##############################################
# symlink が使えないサーバー用のファイルロック
##############################################
#sub lock{
#       local($try) = 3;
#       while(-f $lockfile){
#               if(--$try <= 0){
#                        &error(0);
#               }
#               sleep(1);
#       }
#       open(FILE,">$lockfile") || die &error(2);
#       close(FILE);
#}
##############################################



##### ロック解除
sub unlock{
        unlink($lockfile);
}



##### エラー表示
sub error{
        local($id, $fname)=@_;
        local(@sts) = lstat($lockfile);
        local($tn) = time();
        
        $msg[0] = "ロック中です";
        $msg[1] = "ファイルが開けません";
        $msg[2] = "ファイルに書きこめません";
        $msg[3] = "パラメタが不正です";

        print "Content-type: text/plain\n\n";
        print "<< Error Message from TcounT >>\n";
        print "$msg[$id] ($fname)";
        
        &unlock if ($id); # ID が 0 以外の場合はロック解除
        &unlock if ($tn - $sts[9] > 15); # 約15秒以上ロックが続いてたら自動解除
        exit;
}
