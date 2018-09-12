package %packageName%.controller.bean;

public class JsonResult {
    public enum Code {
        SUCC, FAIL
    }

    private Code code;
    private String msg;

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
