package com.fahdisa.scs.api;

public class ApiResponse<E> {

    private Status status;
    private String description;
    private E data;

    private ApiResponse(Status status, String description, E data) {
        this.status = status;
        this.description = description;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public static class Builder<E> {
        private Status status;
        private String description;
        private E data;

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder data(E data) {
            this.data = data;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(status, description, data);
        }
    }
}
