package com.despegar.p13n.hestia.external.geo;

import java.util.List;

public class PictureDto {

    private String title;
    private String url;
    private String ratio;
    private int order;
    private List<String> tags;



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.order;
        result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        PictureDto other = (PictureDto) obj;
        if (this.order != other.order) {
            return false;
        }
        if (this.url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!this.url.equals(other.url)) {
            return false;
        }
        return true;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRatio() {
        return this.ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


}
