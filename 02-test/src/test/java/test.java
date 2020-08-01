import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by 辉 on 2020/6/8.
 */
public class test {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        //获取数字
//        System.out.println("请输入一个数字");
//        int n = scanner.nextInt();
//        System.out.println("n的值为" + n);
//        System.out.println("请输入一个字符串");
//        String s = scanner.next();
//        System.out.println("s的值为" + s);
//        Random random = new Random();
//        int i = random.nextInt(100);
//        System.out.println("这个随机的整数是："+i);
//        double v = random.nextDouble();
//        System.out.println(v);

        //使用springsecurity加密密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (int  i=0;i<10;i++){
            System.out.println("加密的密码是："+encoder.encode("123"));
        }
    }
}
