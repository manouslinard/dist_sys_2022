package gr.hua.dit.dissys.entity;

public enum ERole {
    ROLE_TENANT {
        public String toString() {
            return "ROLE_TENANT";
        }
    },
    ROLE_LESSOR {
        public String toString() {
            return "ROLE_LESSOR";
        }
    },
    ROLE_ADMIN {
        public String toString() {
            return "ROLE_ADMIN";
        }
    }
}