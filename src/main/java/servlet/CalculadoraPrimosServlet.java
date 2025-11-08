package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
 * Anotación que mapea el Servlet para procesar la solicitud de cálculo.
 */
@WebServlet("/CalculadoraPrimosServlet")
public class CalculadoraPrimosServlet extends HttpServlet {

    /*
     * Método auxiliar para determinar si un número dado es primo.
     */
    private boolean esPrimo(int numero) {
        if (numero <= 1) return false;
        for (int i = 2; i * i <= numero; i++) {
            if (numero % i == 0) return false;
        }
        return true;
    }

    /*
     * Método que procesa la petición enviada por el formulario (POST).
     * @param req Objeto que contiene la petición del cliente.
     * @param resp Objeto que contiene la respuesta del servidor.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Se establece el tipo de contenido para la respuesta del servidor
        resp.setContentType("text/html;charset=UTF-8");

        // Obtenemos los parámetros del formulario y los convertimos a enteros
        int inicio = Integer.parseInt(req.getParameter("inicio"));
        int fin = Integer.parseInt(req.getParameter("fin"));

        // Inicializamos la lista para almacenar los números primos
        List<Integer> primos = new ArrayList<>();

        // Bucle para iterar sobre el rango y calcular los números primos
        for (int i = inicio; i <= fin; i++) {
            if (esPrimo(i)) primos.add(i);
        }

        // Codificamos la lista de primos para pasarla como parámetro de URL al Servlet de PDF
        String primosCodificados = URLEncoder.encode(primos.toString(), StandardCharsets.UTF_8);

        // Obtenemos el escritor para generar la respuesta HTML
        PrintWriter out = resp.getWriter();
        out.println("""
                <html>
                <body>
                <h1>Resultado del Cálculo</h1>
                """);

        // Imprimimos el rango procesado
        out.println("<p>Rango: " + inicio + " a " + fin + "</p>");
        // Imprimimos la lista de números primos encontrados
        out.println("<p>Números primos: " + primos + "</p>");
        // Imprimimos el conteo total de números primos
        out.println("<p>Total encontrados: " + primos.size() + "</p>");

        // Generamos el enlace para descargar el resultado en "PDF", pasando los datos por URL
        out.println("<br><a href='GenerarPDFServlet?inicio=" + inicio +
                "&fin=" + fin +
                "&primos=" + primosCodificados + "'>Descargar PDF</a>");

        // Enlace para volver al formulario principal
        out.println("<br><br><a href='index.html'>Volver</a>");
        out.println("</body></html>");
    }
}