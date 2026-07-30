package physicsml;
import java.io.*; import java.nio.file.*; import java.util.*;
public class Main {
 static final String[] L={"electron","muon","pion","kaon","proton"};
 record E(int y,double[] x){}
 static Random r=new Random(42);
 static double g(double m,double s){return m+r.nextGaussian()*s;}
 static List<E> data(int n){List<E>a=new ArrayList<>(); double[] mass={.000511,.10566,.13957,.49368,.93827};
  for(int y=0;y<5;y++)for(int i=0;i<n;i++){double p=.4+r.nextDouble()*4.6,b=p/Math.sqrt(p*p+mass[y]*mass[y]);
   double tof=3.5/(.299792458*b)+g(0,.08),dedx=(new double[]{1.15,1.55,1.75,2.15,2.65})[y]+.35/(b*b)+g(0,.12);
   double cal=Math.max(0,p*(new double[]{.97,.10,.48,.40,.32})[y]+g(0,.12));
   double len=g((new double[]{2.7,3.4,3.0,2.85,2.65})[y],.17),hits=Math.max(5,Math.round(g((new double[]{18,31,26,23,20})[y],3.5)));
   a.add(new E(y,new double[]{p,dedx,tof,cal,len,hits}));} Collections.shuffle(a,r); return a; }
 static double[] stats(List<E>d,boolean sd,double[] mean){double[]o=new double[6]; for(E e:d)for(int j=0;j<6;j++)o[j]+=sd?Math.pow(e.x[j]-mean[j],2):e.x[j]; for(int j=0;j<6;j++)o[j]=sd?Math.sqrt(o[j]/d.size()):o[j]/d.size(); return o;}
 static double[] z(double[]x,double[]m,double[]s){double[]v=new double[7];v[0]=1;for(int j=0;j<6;j++)v[j+1]=(x[j]-m[j])/Math.max(s[j],1e-9);return v;}
 static double[] prob(double[][]w,double[]x){double[]q=new double[5];double mx=-1e99,sum=0;for(int c=0;c<5;c++){for(int j=0;j<x.length;j++)q[c]+=w[c][j]*x[j];mx=Math.max(mx,q[c]);}for(int c=0;c<5;c++){q[c]=Math.exp(q[c]-mx);sum+=q[c];}for(int c=0;c<5;c++)q[c]/=sum;return q;}
 public static void main(String[]args)throws Exception{List<E>all=data(800);int cut=(int)(all.size()*.8);List<E>tr=all.subList(0,cut),te=all.subList(cut,all.size());double[]m=stats(tr,false,null),s=stats(tr,true,m);double[][]w=new double[5][7];
  for(int ep=1;ep<=600;ep++){double[][]gr=new double[5][7];double loss=0;for(E e:tr){double[]x=z(e.x,m,s),p=prob(w,x);loss-=Math.log(Math.max(p[e.y],1e-12));for(int c=0;c<5;c++)for(int j=0;j<7;j++)gr[c][j]+=(p[c]-(c==e.y?1:0))*x[j];}for(int c=0;c<5;c++)for(int j=0;j<7;j++)w[c][j]-=.08*(gr[c][j]/tr.size()+.0005*w[c][j]);if(ep%100==0)System.out.printf("epoch=%d loss=%.4f%n",ep,loss/tr.size());}
  int[][]cm=new int[5][5];List<String>out=new ArrayList<>();out.add("true,predicted,confidence");for(E e:te){double[]p=prob(w,z(e.x,m,s));int k=0;for(int c=1;c<5;c++)if(p[c]>p[k])k=c;cm[e.y][k]++;out.add(L[e.y]+","+L[k]+","+p[k]);}
  int ok=0;for(int i=0;i<5;i++)ok+=cm[i][i];System.out.printf("Accuracy %.3f%n",ok/(double)te.size());System.out.println("Rows=true, columns=predicted");for(int i=0;i<5;i++)System.out.println(L[i]+" "+Arrays.toString(cm[i]));Files.createDirectories(Path.of("build"));Files.write(Path.of("build/predictions.csv"),out);}
}
