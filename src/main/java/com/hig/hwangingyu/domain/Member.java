package com.hig.hwangingyu.domain;

public class Member {
    private String name;
    private String passwd;
    public Member(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder("");
        strb.append(getName()+" ");

        return strb.toString();
    }
    
    public static class Builder {
        private String name;
        private String passwd;
        
        public Builder() {
            name ="";
            passwd="";

        }
        public Builder setPasswd(String passwd) {
            this.passwd = passwd;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Member build() {
            return new Member(name,passwd);
        }
    }


}
