package com.zzti.lsy.ninetingapp.entity;

import com.zzti.lsy.ninetingapp.base.BaseEntity;

/**
 * 登录成功返回的数据
 */
public class LoginBack extends BaseEntity {
    private class LoginBackData{
        private String userID;
        private String userName;
        private String roleID;
        private String password;
        private String status;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRoleID() {
            return roleID;
        }

        public void setRoleID(String roleID) {
            this.roleID = roleID;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
