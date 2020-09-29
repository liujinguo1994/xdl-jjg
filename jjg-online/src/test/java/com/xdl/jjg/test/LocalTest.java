package com.xdl.jjg.test;


import com.xdl.jjg.util.MD5Utils;

public class LocalTest {


    public static void main(String[] args) throws Exception {

        String url = "jsapi_ticket=kgt8ON7yVITDhtdwci0qeWwPxU8lqFPQa42sW_pPYhWlv8fJ7wvwTpgDLLqJrH5kFrjyrtxCMtw-Bi0vcWct9Q&noncestr=ce922d97572441b4a1ff7d6f9ee&timestamp=1567764282&url=http://jjgtesthttp.xindongle.com/goods/detail?id=152";
        System.out.println(MD5Utils.SHA1Encoder(url));
        String reqInfo = "EK5qtPacjYl3BSanJ1rC0mQtiVTd0Mf0MZ+0VgrKn8xcquCyl8y8wzHfpU3TbGiXFQ0zdAsuKpHtkpOLochhFH6mjFb6FsySnTQ9yRW7qkGe9X8jEr7CjL3wR5BX+Fdndrn1rYxFFd05BnUFSzk49HKafYvupyGgroBkNjLi8+jDS8zQEj4mLWTsCjX8ULUUfiPQx2VzoE2pZzUMBoPLAXUD36g6aGepUyXFHW8HFyG/NjjIm9hI4KP138XFqAXIkferkAbk5WVW42IyzZRV8UVtb/XAGWJMa2cFEUpiTlJJ7I7+aOv0vZx83YuP6ld4Zc8qHQdU3NZ6Ug3zkavA6Mh74KV4LtbzMKSUfWaVnKvYHY0dk6zNXF4RWRmRWkmDYWFe8rrh8ibuXHMauvX0CZO49aL7uVVsLIxqWAVoxy0wtAij7XoAm+j9lmEjE2j7OiVr3LYDid5A9JGC1hM1uJcBxr4S9QaGxRuI9SritasICQsvrPLuur/xgX6uVgSiiJeawpbUonzPsHP4J6ozEZXuiQDysRDFzp9iY/QoUzce0OJ80zWDafxJIdlo5lDhayoIjCEfXqdW5jhOnaYfNo1SkN371fMVORBOFxWOYsIcXaE7cbB3eeZAo0s4AiV7DN9csqxfenoigkMutEz/kV+CO6WZCIDDsYkJoaF427u3YP2EWNqxDTXYgKYO2VAhVr0cyUmUWtoIgNxbMDleAR08LRDFP47MpzHzCtyql2Q1cnoLu0KdTdXJxbTi0y1prtJnJC4jLpcpVlAb/14h+Fx3++P2HiDrAkc8qA7B8DkyudYst2klT66BoPeONKlciDRhgp30BA3LiCUuz7rYXsUYmxot9Mm1wWpwB+LygurM7IoKFoTjGxBK8eHZUEEX27CD/kHxS4mx09UAxjDRB6uu1vnzlkSYP9EE0YE5nn+GlNKH8y4AVZFqi3BIAgKOGToIAQRZOgUc5p6hMsPPnKV/DFXmEGRlwhx8bzfpGuerimASEeWrPvb5s17+foTRepl9S++bCH7Rc2LXNi3M9Tq04GeVV+qf1nNVCQ8hDpQ=";
        String key = "ebf814d105cc4ab288d330ead183c42d";
        System.out.println(WeChatPayUtils.getReqInfo(reqInfo, key));

    }


}
