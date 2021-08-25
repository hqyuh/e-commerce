package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExportUser extends AbstractExporter{

    public void export(List<User> listUsers,
                       HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        final String[] csvHeader = { "User ID", "Email", "First Name", "Last Name", "Roles", "Enabled" };
        final String[] fieldMapping = { "id", "email", "firstName", "lastName", "roles", "enabled" };

        csvWriter.writeHeader(csvHeader);

        for (User user: listUsers) {
            csvWriter.write(user, fieldMapping);
        }

        csvWriter.close();

    }

}
