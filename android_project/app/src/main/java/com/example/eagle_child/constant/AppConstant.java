package com.example.eagle_child.constant;

import com.example.eagle_child.R;

import java.util.ArrayList;

public class AppConstant {

    public static int MIN_TIME_INTERVAL_FOR_SPLASH = 3000; // in millis
    public static int MIN_TIME_VERIFY_CODE = 6000; // in  public static final int SELECT_IMAGE_COUNT = 1; millis
    public static final int SELECT_IMAGE_COUNT = 1;
    public static final String PhonePattern = "^(?:\\+971|00971|0)?(?:50|51|52|55|56|2|3|4|6|7|9)\\d{7}$";
    public static final String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        public class TRANSITION_TYPES {
            public static final int NONE = 2000;
            public static final int FADE = 20001;
            public static final int SLIDE = 20002;
        }



        public class TOAST_TYPES {
        public static final int INFO = 1101;
        public static final int SUCCESS = 1102;
        public static final int ERROR = 1103;

    }



    public class ServerAPICalls {



        public static final String BASE_URL =  "https://mip-api-2.herokuapp.com/";

        public static final String GALLERY = "";
        public static final String FILE_UPLOAD = "file-upload";


    }



}
