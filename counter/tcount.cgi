#!/usr/bin/perl
#  
######################################################################
###
###
###  CGI�����󥿡� TcounT Ver.4.20
###     [1/3] ���� (tcount.cgi)
###                                 (c) 1996-1999 Takahiro Nishida
###                                 http://www.mytools.net/
###
###
######################################################################
#
### �ѿ������� �ʾܺ٤Ͼ嵭�ڡ��������������� ######################

$basedir = ".";

### �ѿ������� �ʤ����ޤǡ�###########################################

require './gifcat.pl';

$lockfile="$basedir/lockdir/tc.lock";
$compfile="$basedir/tccomp.txt";
$logfile="$basedir/tclog.txt";
$timefile="$basedir/tcmtime.txt";


&main;



##### �ᥤ��
sub main{
        &lock;
        &check_input;
        &open_counter;
        &increase_counter;
        &show_counter;
        &unlock;
}



##### ���ϤΥ����å�
sub check_input{
        $buffer = $ENV{'QUERY_STRING'};
        @prm = split("&", $buffer);
        
        $cname = ($prm[0] =~ /^\w+$/) ? $prm[0] : '';
        $incr  = ($prm[1] == 1)       ? 1       : 0;
        $keta  = ($prm[2] =~ /^\d+$/) ? $prm[2] : 5;
        $type  = ($prm[3] =~ /^\w+$/) ? $prm[3] : 'img';
        $obj   = ($prm[4] =~ /^\w+$/) ? $prm[4] : 'all';
        
        # ������̾�����ꤵ��Ƥ��ʤ��ȥ��顼
        ($cname) || &error(3);
}



#### �����󥿡��ե�����򳫤�
sub open_counter{
        $cnt = &openfile("$basedir/data/$obj/$cname.txt");
}



##### ������ȥ��å�
sub increase_counter{
        # ���åե饰��Ω�äƤ�����Τ߼¹�
        return unless ($incr && $obj eq 'all');
        
        # ���դ��Ѥ�äƤ��뤫�����å� (�����ȽŤ��ΤǤ�����)
        &check_date;
        
        $cnt++;
        &updatefile("$basedir/data/all/$cname.txt", $cnt);
        
        # �����Υ�����ȿ�������
        #���ɤ߹��ޤ�륿���ߥ󥰤ˤ�äƤϡ�ɽ��������뤳��ͭ���
        local($ctoday) = &openfile("$basedir/data/today/$cname.txt");
        $ctoday++;
        &updatefile("$basedir/data/today/$cname.txt", $ctoday);
}



##### ���ե����å�
sub check_date{
        local(@ts);
        @ts = localtime(time);
        $ts[4]++;
        $ts[5] += 1900; # 2000ǯ������б��ʾС�
        ($ts[4] < 10) && ($ts[4] = "0$ts[4]");
        ($ts[3] < 10) && ($ts[3] = "0$ts[3]");
        $date_now = "$ts[5]$ts[4]$ts[3]";
        $date_old = &openfile($timefile) || "00000000";
        
        # ���դ��Ѥ�äƤ���������
        ($date_now <= $date_old) || &update_summery;
}



##### �����󥿡�������ɽ��
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



##### �������ä��Ȥ��ν���
sub update_summery{
        &updatefile($timefile, $date_now); # timefile�ν񤭤ʤ���
        
        return if ($date_old == 0); # �����
        return if ($date_old =~ /\//); # 4.10 -> 4.20
        
        # ������̾�������ޤǡ���硢�����Υ�����ȿ�������ѥХåե������Хåե�
        local($cn, $cold, $call, $cday, $compbuf, $logbuf);
        # �ƥ�ݥ��
        local(@tmps);
        
        # ����ѥե�����Υ����ץ�
        open(FILE,"$compfile") || die &error(1, $compfile);
        @tmps = <FILE>;
        close(FILE);
        
        $logbuf  = $date_old;
        $logbuf .= "\t";
        
        foreach(@tmps){
                # �ե�����̾�������ޤǤΥ�����ȿ�
                ($cn, $cold) = split("\t");
                # ��祫����ȿ��μ���
                $call = &openfile("$basedir/data/all/$cn.txt");
                # �����Υ�����ȿ��η׻���today�����Ƥˤ��ʤ���
                $cday = $call - $cold;
                # ����ѥե����������
                $compbuf .= "$cn\t$call\t\n";
                # ���ե����������
                $logbuf .= "[$cn]$cday($call)\t";
                # �����Υ�����ȿ��ι���
                &updatefile("$basedir/data/day/$cn.txt", $cday);
                # �����Υ�����ȿ��Υꥻ�å�
                &updatefile("$basedir/data/today/$cn.txt", "0");
        }
        
        # �����ɲ�
        $logbuf .= "\n";
        open(FILE, ">>$logfile") || die &error(2, $logfile);
        print FILE $logbuf;
        close(FILE);
        
        &updatefile($compfile, $compbuf); # ����ѥե�����ι���
}



##### ���ꤵ�줿�����󥿡��ο��ͤ�����
sub openfile{
        local($cfile) = @_;
        local($cnt);
        open(FILE, "$cfile") || die &error(1, $cfile);
        $cnt = <FILE>;
        close(FILE);
        $cnt;
}



##### ���ꤵ�줿�����󥿡��˿��ͤ������
sub updatefile{
        local($cfile, $cnt) = @_;
        open(FILE, ">$cfile") || die &error(2, $cfile);
        print FILE $cnt;
        close(FILE);
}



##### �ե������å�
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
# symlink ���Ȥ��ʤ������С��ѤΥե������å�
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



##### ��å����
sub unlock{
        unlink($lockfile);
}



##### ���顼ɽ��
sub error{
        local($id, $fname)=@_;
        local(@sts) = lstat($lockfile);
        local($tn) = time();
        
        $msg[0] = "��å���Ǥ�";
        $msg[1] = "�ե����뤬�����ޤ���";
        $msg[2] = "�ե�����˽񤭤���ޤ���";
        $msg[3] = "�ѥ�᥿�������Ǥ�";

        print "Content-type: text/plain\n\n";
        print "<< Error Message from TcounT >>\n";
        print "$msg[$id] ($fname)";
        
        &unlock if ($id); # ID �� 0 �ʳ��ξ��ϥ�å����
        &unlock if ($tn - $sts[9] > 15); # ��15�ðʾ��å���³���Ƥ��鼫ư���
        exit;
}
