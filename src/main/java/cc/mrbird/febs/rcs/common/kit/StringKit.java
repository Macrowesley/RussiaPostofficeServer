package cc.mrbird.febs.rcs.common.kit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringKit {
    /**
     * 字符串按指定长度换行
     * @param content
     * @param len
     * @return
     */
    public static String splitString(String content, int len) {
        String tmp = "";
        if (len > 0) {
            if (content.length() > len) {
                int rows = (content.length() + len - 1) / len;
                for (int i = 0; i < rows; i++) {
                    if (i == rows - 1) {
                        tmp += content.substring(i * len);
                    } else {
                        tmp += content.substring(i * len, i * len + len) + "\n";
                    }
                }
            } else {
                tmp = content;
            }
        }
//        log.info("换行后，内容长度：" + tmp.length());
        return tmp;
    }

    public static void main(String[] args) throws Exception {
        String str = "Заявка на выделение серверных ресурсовЗаявка на выделение серверных ресурсовЗаявка на выделение серверных ресурсовЗаявка на выделение серверных ресурсовЗаявка на выделение серверных ресурсов";

        log.info("\n" + StringKit.splitString(str, 70));

        String test = "Заявка на выделение серверных ресурсовЗаявка на выделение се\\r\\nрверных ресурсовЗаявка на выделение серверных ресурсовЗаявка\\r\\n на выделение серверных ресурсовЗаявка на выделение серверны\\r\\nх ресурсовв ыделение серверных ресурсов выделение серверных ";
    }
}
