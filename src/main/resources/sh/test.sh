#!/bin/bash

# 变量，循环
#for i in `ls /etc |grep shadow`; do
#  echo $i
#done

# 传参

#for i in "$*"; do
#  echo $i
#done

#for i in "$@"; do
#  echo $i
#done

# 数组
#arr=(2 3 4 dfs dssds rwew)
#echo ${arr[1]}
#echo ${arr[@]}
#echo ${#arr[@]}

# 运算
a=19
b=20
#echo `expr $a + $b`
##=和!=比较的是字符串
#echo `expr $a != $b`
#echo `expr $a -eq $b` #报错
#if [ $a \< $b ] 
#then
#  echo "满足"
#else
#  echo "不满足"
#fi
#[]必须带空格 then必须换行,或用;分号表示下一行
#if [ $a -eq $b ]; then
#  echo "相等"
#else
#  echo "不等"
#fi
#布尔运算
#if [ $a -gt 900 -a $b -lt 100 ] 
#then
#  echo "满足"
#else
#  echo "不满足"
#fi
#逻辑运算
#if [[ $a -gt 9 && $b -lt 100 ]] 
#then
#  echo "满足"
#else
#  echo "不满足"
#fi

# printf输出
#printf "%-10s %-8s:%d %f\n" dsfs sd2d 234 12.3
## 转义替换
#printf "a string, no processing:<%s>\n" "A\nB"
#printf "a string, no processing:<%b>\n" "A\nB"

# test运算
#if test $a -gt 9 -a $b -eq 20
#then
#  echo "is true"
#else
#  printf "is false\n"
#fi

# []运算
#value=[$a+$b]
#echo value

# 循环
#i=0
#until [ ! $i -lt 10 ]
#do
#  echo $i
#  if [ $i -ge 5 ]
#  then
#    echo 'out'
#    break
#  fi
#  i=`expr $i + 1`;
#done

#/bin/dash下无效,尽量不用
#for ((i=0; i<10; i++))
#do
#  echo $i
#done

#i=0
#while :
#do
#  echo $i
#  i=`expr $i + 1`;
#  if [ $i -ge 5 ]
#  then
#    echo 'out'
#    break
#  fi
#done

# echo 单引号 原文输出 包括$
# 函数
#customFun(){
#  echo "参数1 $1"
#  echo "参数2 $2"
#  return $(($1+$2))
#}
#customFun 1 2
#echo "结果 $?"

# 输入输出重定向
#who > test.txt

# Here Document(文本内注释也会执行)
#user=ulane
#password=***
#host=***
#cat << EOF
#test1
#test2
#EOF
##select * from tb_service where rownum < 2;
##sqlplus /nolog << EOF > test.txt
#sqlplus /nolog << EOF
#conn ${user}/${password}@${host}:1521/helowinXDB;
#show user;
#update tb_service set ROOTSERVICE_NAME = '个的额' where serviceid = 1;
#commit;
#EOF

#Here Document 调用传参
#c=$1
#if [ $c ]
#then
#  echo "参数c=$c"
#else
#  echo "default c=1"
#  c=1
#fi

#sqlplus /nolog << EOF
#conn ${user}/${password}@${host}:1521/helowinXDB;
#show user;
#update tb_service set ROOTSERVICE_NAME = '个的额' where serviceid = $c;
#commit;
#EOF

#. ./file.sh
#source ./file.sh
#
#echo "d:$d"
#
#echo "参数2 $2"

# ctl sqlldr
if [ ! -e ./ctl/log/normal ]
then
  mkdir -p ctl/log/normal
fi
if [ ! -e ./ctl/log/bad ]
then
  mkdir -p ctl/log/bad
fi
sqlldr ${user}/${password}@${host}:1521/helowinXDB control=ctl/test.ctl log=ctl/log/normal/test.log bad=ctl/log/bad/test.log data=ctl/loader.txt

