package cn.etsoft.smarthome.domain;

/**
 * Created by Say GoBay on 2016/8/24.
 */
public class AddBean {
        private int imageId;
        private String titleId;

        public AddBean() {
            super();
        }

        public AddBean(int imageId, String titleId) {
            super();
            this.imageId = imageId;
            this.titleId = titleId;
        }

        public String getTitle() {
            return titleId;
        }

        public void setTitle(String title) {
            this.titleId = title;
        }

        public int getImageId() {
            return imageId;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

}
