package servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/*
 * Mapeo del Servlet encargado de recibir los resultados y generar el archivo PDF.
 * Esta clase requiere la librería iText para funcionar.
 */
@WebServlet("/GenerarPDFServlet")
public class GenerarPDFServlet extends HttpServlet {

    /*
     * Método que procesa la petición de descarga (GET).
     * Recibe los datos ya calculados desde CalculadoraPrimosServlet y genera el PDF.
     * @param req Objeto que contiene la petición del cliente (incluyendo parámetros).
     * @param resp Objeto que contiene la respuesta del servidor (en este caso, el archivo PDF).
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Obtenemos los parámetros de rango
        String inicio = req.getParameter("inicio");
        String fin = req.getParameter("fin");

        // Obtenemos y decodificamos la lista de primos pasada como parámetro de URL
        String primosDecodificados = URLDecoder.decode(req.getParameter("primos"), StandardCharsets.UTF_8);

        // --- CABECERAS ESENCIALES PARA FORZAR LA DESCARGA DE UN PDF ---

        // Se establece el tipo de contenido como PDF
        resp.setContentType("application/pdf");

        // Cabecera que obliga al navegador a descargar el archivo con el nombre especificado
        resp.setHeader("Content-Disposition", "attachment; filename=resultados_primos.pdf");

        try {
            // 1. Crear el objeto Documento de iText
            Document document = new Document();

            // 2. Asociar el escritor de PDF al flujo de salida de la respuesta HTTP
            PdfWriter.getInstance(document, resp.getOutputStream());

            // 3. Abrir el documento para empezar a escribir contenido
            document.open();

            // 4. Agregar contenido al documento
            document.add(new Paragraph("Resultado de cálculo de números primos"));
            document.add(new Paragraph("Rango: " + inicio + " a " + fin));
            document.add(new Paragraph("Números primos: " + primosDecodificados));

            /* * Se calcula la cantidad total de primos dividiendo la cadena por las comas (',')
             * y obteniendo el número de elementos.
             */
            document.add(new Paragraph("Cantidad total: " + primosDecodificados.split(",").length));

            // 5. Cerrar el documento y liberar los recursos (esto envía el archivo al cliente)
            document.close();

        } catch (Exception e) {
            // Manejo de cualquier excepción relacionada con la generación del PDF o iText
            throw new IOException("Error al generar el archivo PDF: " + e.getMessage());
        }
    }
}