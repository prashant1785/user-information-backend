package com.user.information.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.user.information.entity.Device;
import com.user.information.entity.User;
import com.user.information.repository.DeviceRepository;
import com.user.information.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public byte[] generateUsersPdf() {

        List<User> users = userRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate());

        PdfWriter.getInstance(document, out);

        document.open();

        Font title =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

        Paragraph p =
                new Paragraph("User Information Report", title);

        p.setAlignment(Element.ALIGN_CENTER);

        document.add(p);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);

        table.setWidthPercentage(100);

        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Email");
        table.addCell("Phone");
        table.addCell("Address");
        table.addCell("Role");

        for (User user : users) {

            table.addCell(String.valueOf(user.getId()));

            table.addCell(user.getFullName());

            table.addCell(user.getEmail());

            table.addCell(user.getPhone());

            table.addCell(user.getAddress());

            table.addCell(user.getRole().name());

        }

        document.add(table);

        document.close();

        return out.toByteArray();

    }

    @Override
    public byte[] generateDevicesPdf() {

        List<Device> devices = deviceRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate());

        PdfWriter.getInstance(document, out);

        document.open();

        Font title =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

        Paragraph p =
                new Paragraph("Device Report", title);

        p.setAlignment(Element.ALIGN_CENTER);

        document.add(p);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);

        table.setWidthPercentage(100);

        table.addCell("Device ID");

        table.addCell("User");

        table.addCell("Event");

        table.addCell("Start");

        table.addCell("End");

        table.addCell("Details");

        for (Device d : devices) {

            table.addCell(d.getDeviceId());

            table.addCell(d.getUser().getFullName());

            table.addCell(d.getEventType().name());

            table.addCell(String.valueOf(d.getStartTime()));

            table.addCell(String.valueOf(d.getEndTime()));

            table.addCell(d.getDetails());

        }

        document.add(table);

        document.close();

        return out.toByteArray();


    }
}
