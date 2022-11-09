package Servlet;

import clase.Productos;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author FELICIA
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();

        if (sesion.getAttribute("ListaProducto") == null) {
            ArrayList<Productos> listaux = new ArrayList<Productos>();
            sesion.setAttribute("ListaProducto", listaux);

        }

        ArrayList<Productos> lista = (ArrayList<Productos>) sesion.getAttribute("ListaProducto");

        String op = request.getParameter("op");
        String opcion = (op != null) ? request.getParameter("op") : "view";

        Productos obj1 = new Productos();
        int id, pos;

        switch (opcion) {
            case "nuevo"://insertar nuevo registro
                request.setAttribute("MiProducto", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;

            case "editar"://Modifiacar registro
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request, id);
                obj1 = lista.get(pos);
                request.setAttribute("MiProducto", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "eliminar"://Eliminar registro
                pos = buscarIndice(request, Integer.parseInt(request.getParameter("id")));
                lista.remove(pos);
                sesion.setAttribute("ListaProducto", lista);
                response.sendRedirect("index.jsp");
                break;
            case "view":
                response.sendRedirect("index.jsp");

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession ses = request.getSession();
        ArrayList<Productos> lista = (ArrayList<Productos>) ses.getAttribute("ListaProducto");

        Productos obj1 = new Productos();

        obj1.setId(Integer.parseInt(request.getParameter("id")));
        obj1.setDescripcion(request.getParameter("descripcion"));
        obj1.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        obj1.setPrecio(Double.parseDouble(request.getParameter("precio")));
        obj1.setCategoria(request.getParameter("categoria"));

        int idt = obj1.getId();

        if (idt == 0) {
            int ultID;
            ultID = ultimoId(request);
            obj1.setId(ultID);
            lista.add(obj1);
        } else {
            lista.set(buscarIndice(request, idt), obj1);
        }
        ses.setAttribute("ListaProducto", lista);
        response.sendRedirect("index.jsp");

    }

    private int ultimoId(HttpServletRequest request) {
        HttpSession ses = request.getSession();
        ArrayList<Productos> lista = (ArrayList<Productos>) ses.getAttribute("ListaProducto");

        int idaux = 0;

        for (Productos item : lista) {
            idaux = item.getId();
        }
        return idaux + 1;
    }

    private int buscarIndice(HttpServletRequest request, int id) {
        HttpSession ses = request.getSession();
        ArrayList<Productos> lista = (ArrayList<Productos>) ses.getAttribute("ListaProducto");

        int i = 0;
        if (lista.size() > 0) {
            while (i < lista.size()) {
                if (lista.get(i).getId() == id) {
                    break;
                } else {
                    i++;
                }
            }
        }
        return i;
    }

    private static class Productos {

        public Productos() {
        }
    }

}
