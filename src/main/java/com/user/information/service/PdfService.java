package com.user.information.service;

public interface PdfService {

    byte[] generateUsersPdf();

    byte[] generateDevicesPdf();
}
