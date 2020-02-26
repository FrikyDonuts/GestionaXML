package gestionaxml;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.awt.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * <h1>GestionXML</h1>
 * Esta es una clase para gestionar ficheros XML (Funciona solo a un nivel de profundidad).
 * <pre>
 * {@code
 * <xmp>
 * Ej fichero correcto:
 * <Productos>
 *   <Producto>
 *       <Nombre>Peonias</Nombre>
 *       <Precio>4</Precio>
 *       <Icono>peonias.png</Icono>
 *   </Producto>
 *   <Producto>
 *       <Nombre>Rosas</Nombre>
 *       <Precio>2</Precio>
 *       <Icono>rosas.png</Icono>
 *   </Producto>
 *  </Productos>
 * </xmp>
 * <xmp>
 * }
 * </pre>
 * Ej fichero incorrecto:
 * <pre>
 * {@code
 * <Productos>
 *   <Producto>
 *       <flor>
 *           <Nombre>Peonias</Nombre>
 *           <Precio>4</Precio>
 *           <Icono>peonias.png</Icono>
 *       </flor>
 *   </Producto>
 *   <Producto>
 *       <flor>
 *           <Nombre>Rosas</Nombre>
 *           <Precio>2</Precio>
 *           <Icono>rosas.png</Icono>
 *       </flor>
 *   </Producto>
 *  </Productos>
 * </xmp>
 * }
 * </pre>
 * @author neowavila
 * @version 2.8.0
 */
public class GestionaXML 
{
    private final File fichero;
    private Node[] hijosRaiz;
    private final Node raiz;
    private Document doc;

    /**
     * Constructor que abre el fichero e inicializa las variables que nos permiten empezar a trabajar con el.
     * @param rutaXML ruta del fichero XML que queremos trabajar.
     */
    public GestionaXML(String rutaXML) 
    {
        fichero=new File(rutaXML);
        if(abrir())
        {
            System.out.println("Archivo abierto con extio");
            raiz=doc.getFirstChild();
            hijosRaiz=getNodosRaiz();
        }
        else
        {
            raiz = null;
            hijosRaiz = null;
            System.out.println("Error abriendo el archivo");
        }
    }                                                                                         

    /**
     * Abre el fichero XML y nos permite su lectura/escritura de datos.
     * @return si ha funcionado o no.
     */
    private boolean abrir()
    {   
        try
        {
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder=factory.newDocumentBuilder();
            doc=builder.parse(fichero);
            return true;                                                    
        }
        catch(IOException | ParserConfigurationException | SAXException e)
        {
            System.out.println(e.toString());
            return false;
        }
    }
    /**
     * Devuelve un array de Strings con todos los datos de un nodo.
     * @param n Nodo del que queremos los datos.
     */
    private String[] procesaNodo(Node n)
    {
        NodeList nodos=n.getChildNodes();
        List datosNodo = new List();
        
        for(int i=0; i<nodos.getLength(); i++)
        {
            Node ntemp = nodos.item(i);
            if(ntemp.getNodeType()==Node.ELEMENT_NODE)
            {
                //String tituloInfo=ntemp.getNodeName();
                String info=ntemp.getChildNodes().item(0).getNodeValue();
                datosNodo.add(info);
            }
        }  
        return datosNodo.getItems();
    }
    /**
     * @deprecated mejor usar getIndiceNodo para no tener que trabajar con nodos.
     * Busca en la lista de hijosRaiz y devuelve el primer nodo que coincida con
     * el campo y el valor.
     * @param campo campo a buscar.
     * @param valorCampo valor del campo a buscar.
     * @return Nodo que tiene contiene el campo y valor.
     */
    private Node buscaNodo(String campo, String valorCampo)
    {
       Node node;
       
       for (int i=0; i<hijosRaiz.length; i++) //Proceso los nodos hijo
       { 
           node = hijosRaiz[i];
           if(node.getNodeType()==Node.ELEMENT_NODE)
           {
               //Es un nodo libro que hay que procesar si es de tipo Elemento
               String valorCampoNodo = GestionaXML.this.getValorCampo(node, campo);
               if(valorCampo.equals(valorCampoNodo))
                   return node;
            }
       }
        return null;
    }
    /**
     * Cuenta cuantos nodos de tipo elemento hay en la lista de nodos hijos del raiz.
     * @return Devuelve cuantos nodos de tipo elemento hay en la lista de nodos hijos del raiz.
     */
    private int cuentaNodos()
    {
        NodeList listaNodos = raiz.getChildNodes();
        int contador=0;
        for (int i = 0; i < listaNodos.getLength(); i++) 
        {
            Node node = listaNodos.item(i);
            if(node.getNodeType()==Node.ELEMENT_NODE)
                contador++;
        }
        return contador;
    }
    /**
     * Devuelve un array con todos los nodos hijos de tipo element del nodo raiz.
     * @return un array con todos los nodos hijos de tipo element del nodo raiz.
     */
    private Node[] getNodosRaiz()
    {
        NodeList listaNodos = raiz.getChildNodes();
        Node[] nodos= new Node[cuentaNodos()];
        int contador=0;
        
        for (int i = 0; i < listaNodos.getLength(); i++) 
        {
            Node node = listaNodos.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE)
            {
                nodos[contador]=listaNodos.item(i);
                contador++;
            }
        }
        
        return nodos;
    }
    /**
     * Devuelve el valor de un campo contenido en un nodo.
     * @param n Un nodo.
     * @param campo titulo del campo que quieres buscar.
     * @return valor del campo buscado
     */
    private String getValorCampo(Node n, String campo)
    {
        //Obtiene los hijos del Libro (titulo y autor)
        NodeList nodos=n.getChildNodes();
        Node ntemp;
        
         for (int i=0; i<nodos.getLength(); i++)
         {
             ntemp = nodos.item(i);
             if(ntemp.getNodeType()==Node.ELEMENT_NODE)
             {
                 String tituloCampo=ntemp.getNodeName();
                 if(tituloCampo.equals(campo))
                     return ntemp.getChildNodes().item(0).getNodeValue();
             }
         }
         return "No se ha encontrado el campo";
    }
    /**
     * Verifica que num exista en el array numeros.
     * @param num numero a buscar en el array.
     * @param numeros array donde buscamos el numero.
     * @return true o false en funcion de si se encuentra num en el array.
     */
    private boolean compruebaNum(int num, int[] numeros)
    {
        for (int i = 0; i < numeros.length; i++) {
            int numero = numeros[i];
            if(num==numero)
                return false;
        }
        return false;
    }
    
    /**
     * Devuelve el valor de un campo contenido en un nodo.
     * @param indice posicion del nodo en el array hijosRaiz.
     * @param campo titulo del campo que quieres buscar.
     * @return valor del campo buscado
     */
    public String getValorCampo(int indice, String campo)
    {
        NodeList nodos=this.hijosRaiz[indice].getChildNodes();
        Node ntemp;
        
        for (int i=0; i<nodos.getLength(); i++)
        {
            ntemp = nodos.item(i);
            if(ntemp.getNodeType()==Node.ELEMENT_NODE)
            {
                String tituloCampo=ntemp.getNodeName();
                if(tituloCampo.equals(campo))
                    return ntemp.getChildNodes().item(0).getNodeValue();
            }
        }
        return "No se ha encontrado el campo";
    }
    
    
    /**
     * Devuelve un array de Strings con el contenidos de los nodos hijos de la 
     * lista de hijos principal. 
     * <pre>
     * {@code
     * <xmp>
     * Ej XML:
     * <BDProductos>
     *  <Producto>
     *      <Nombre>Rosa</Nombre>
     *      <Precio>1</Precio>
     *  </Producto>
     *  <Producto>
     *      <Nombre>Lirio</Nombre>
     *      <Precio>2</Precio>
     *  </Producto>
     * </BDProductos>
     * </xmp>
     * }
     * </pre>
     * Ej String[]: {"Rosa-1","Lirio-2"}
     * @return String[] valores de los nodos hijos de la lista de hijos principal.
     */
    public String[] getValorNodos()
    {
       int contador=0;
       String datos[]= new String[hijosRaiz.length];
       
       for(int i=0; i<hijosRaiz.length; i++)
       { 
            Node node = hijosRaiz[i];
            String []datosNodo=procesaNodo(node);
            //Inicializamos a vacio para que al hacer la suma no pille el null.
            datos[contador]="";
            //Guardamos los datos en salida.
            for(int j=0; j<datosNodo.length; j++)
                datos[contador]+=datosNodo[j]+"-";
            //Eliminamos el ultimo "-".
            datos[contador]=datos[contador].substring(0, datos[contador].length()-1);

            contador++;
       }

       return datos;
    }
    /**
     * Devuelve un array de Strings con el contenidos de los nodos hijos de la 
     * lista de hijos principal.
     * <pre>
     * {@code
     * <xmp>
     * Ej XML:
     * <BDProductos>
     *  <Producto>
     *      <Nombre>Rosa</Nombre>
     *      <Precio>1</Precio>
     *  </Producto>
     *  <Producto>
     *      <Nombre>Lirio</Nombre>
     *      <Precio>2</Precio>
     *  </Producto>
     * </BDProductos>
     * </xmp>
     * }
     * </pre>
     * Ej String[]: {"Rosa-1","Lirio-2"}
     * @return String[] valores de los nodos hijos de la lista de hijos principal.
     * @param separador separador para los valores del String.
     */
    public String[] getValorNodos(String separador)
    {
       int contador=0;
       String datos[]= new String[hijosRaiz.length];
       
       for(int i=0; i<hijosRaiz.length; i++)
       { 
           Node node = hijosRaiz[i];
           if(node.getNodeType()==Node.ELEMENT_NODE)
           {
               String []datosNodo=procesaNodo(node);
               //Inicializamos a vacio para que al hacer la suma no pille el null.
               datos[contador]="";
               //Guardamos los datos en salida.
               for(int j=0; j<datosNodo.length; j++)
                   datos[contador]+=datosNodo[j]+separador;
               //Eliminamos el ultimo "-".
               datos[contador]=datos[contador].substring(0, datos[contador].length()-1);
               
               contador++;
            }
       }

       return datos;
    }
    /**
     * Devuelve un array de Strings con todos los datos de un nodo.
     * @param indiceNodo numero del nodo en el array de hijosRaiz.
     * @return array con los valores del nodo.
     */
    public String[] getValoresNodo(int indiceNodo)
    {
        Node n = this.hijosRaiz[indiceNodo];
        NodeList nodos=n.getChildNodes();
        List datosNodo = new List();
        
        for(int i=0; i<nodos.getLength(); i++)
        {
            Node ntemp = nodos.item(i);
            if(ntemp.getNodeType()==Node.ELEMENT_NODE)
            {
                //String tituloInfo=ntemp.getNodeName();
                String info=ntemp.getChildNodes().item(0).getNodeValue();
                datosNodo.add(info);
            }
        }  
        return datosNodo.getItems();
    }
    /**
     * Devuelve un String[numCamposNodo][2] donde contiene el nombre de los campos
     * del nodo y la informacion que contiene en formato:
     * String[n][0] = Campo
     * String[n][1] = Informacion.
     * @param indiceNodo nodo del que quremos la informacion.
     * @return String[numCamposNodo][2] donde contiene el nombre de los campos
     * del nodo y la informacion que contiene.
     */
    public String[][] getNodo(int indiceNodo)
    {
        Node n = this.hijosRaiz[indiceNodo];
        NodeList nodos=n.getChildNodes();
        List datosNodo = new List();
        List camposNodo = new List();
        
        for(int i=0; i<nodos.getLength(); i++)
        {
            Node ntemp = nodos.item(i);
            if(ntemp.getNodeType()==Node.ELEMENT_NODE)
            {
                String tituloInfo=ntemp.getNodeName();
                String info=ntemp.getChildNodes().item(0).getNodeValue();
                datosNodo.add(info);
                camposNodo.add(tituloInfo);
            }
        }
        String[][] infoNodo = new String[camposNodo.getItemCount()][2];
        for (int i = 0; i < infoNodo.length; i++) 
        {
            infoNodo[i][0] = camposNodo.getItem(i);
            infoNodo[i][1] = datosNodo.getItem(i);
        }
        return infoNodo;
    }
    /**
     * Devuelve la longitud del array hijosRaiz (numero de nodos de tipo element).
     * @return longitud del array hijosRaiz.
     */
    public int getNumNodos()
    {
        return hijosRaiz.length;
    }
    /**
     * Borra el nodo seleccionado (Los cambios hay que guardarlos manualmente).
     * @param indiceNodo indice del nodo a borrar.
     * @return true si se ha borrado el nodo con exito.
     */
    public boolean borraNodo(int indiceNodo)
    {
        try
        {
            Node nodo = hijosRaiz[indiceNodo];
            raiz.removeChild(nodo);
            return true;
        }
        catch(DOMException e)
        {
            return false;
        }
        catch(ArrayIndexOutOfBoundsException i)
        {
            return false;
        }
    }
    /**
     * Guarda los cambios en la ruta enviada.
     * @param rutaFichero ruta donde se desea guardar el fichero.
     * @return true en caso de que se guarde correctamente.
     */
    public boolean guardar(String rutaFichero)
    {
        try
        {
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(rutaFichero),format);
            serializer.serialize(doc);
            return true;
        }
        catch(Exception e) 
        {   
           return false;
        }
    }
    /**
     * Guarda los cambios sobreescribiendo el fichero.
     * @return true en caso de que se guarde correctamente.
     */
    public boolean guardar()
    {
        try
        {
            String rutaFichero = fichero.getAbsolutePath();
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(rutaFichero),format);
            serializer.serialize(doc);
            return true;
        }
        catch(Exception e) 
        {   
           return false;
        }
    }
    /**
     * Crea un hijo de raiz con los datos del array.   
     * @param datos array que contiene los datos del nodo.
     * Tiene que ser en el formato String[n campos][2],
     * donde en la fila se guardan los campos y en las columnas los valores.
     * @param tituloNodo nombre del nodo.
     * @return true en caso de que se logre crear el nodo con exito.
     */
    public boolean creaNodo(String tituloNodo, String[][] datos)
    {
        if(datos[0].length==2)
        {
            Element elePadre = doc.createElement(tituloNodo);
            
            for(int i = 0; i < datos.length; i++) 
            {
                String titulo=null;
                String valor=null;
                try
                {
                    titulo = datos[i][0];
                    valor = datos[i][1];
                }
                catch(NullPointerException e)
                {
                    return false;
                }
                Element element = doc.createElement(titulo);
                Text text = doc.createTextNode(valor);
                element.appendChild(text);
                elePadre.appendChild(element);
            }
            raiz.appendChild(elePadre);
            return true;
        }
        else
            return false;
    }
    /**
     * Crea un hijo de raiz con los datos del array.
     * @param tituloNodo nombre del nodo.
     * @param datos array que contiene los datos del nodo.
     * Tiene que ser en el formato String[n campos][2],
     * donde en la fila se guardan los campos y en las columnas los valores.
     * @param saltaIndices lista con los indices que tiene que saltarse 
     * en el array de datos[][] (estos no seran agregados al xml).
     * @return true en caso de que se logre crear el nodo con exito.
     */
    public boolean creaNodo(String tituloNodo, String[][] datos, int []saltaIndices)
    {        
        if(datos[0].length==2)
        {
            Element elePadre = doc.createElement(tituloNodo);
            
            for(int i = 0; i < datos.length; i++) 
            {
                if(compruebaNum(i, saltaIndices))
                    continue;
                
                String titulo;
                String valor;
                try
                {
                    titulo = datos[i][0];
                    valor = datos[i][1];
                }
                catch(NullPointerException e)
                {
                    return false;
                }
                
                Element element = doc.createElement(titulo);
                Text text = doc.createTextNode(valor);
                element.appendChild(text);
                elePadre.appendChild(element);
            }
            raiz.appendChild(elePadre);
            return true;
        }
        else
            return false;
    }
    /**
     * Crea un hijo de raiz con los datos del array.
     * @param tituloNodo nombre del nodo.
     * @param datos array que contiene los datos del nodo.
     * Tiene que ser en el formato String[n campos][2],
     * donde en la fila se guardan los campos y en las columnas los valores.
     * @param saltaIndices indice de la lista de datos que no queremos agregar.
     * @return true en caso de que se logre crear el nodo con exito.
     */
    public boolean creaNodo(String tituloNodo, String[][] datos, int saltaIndices)
    {        
        if(datos[0].length==2)
        {
            Element elePadre = doc.createElement(tituloNodo);
            
            for(int i = 0; i < datos.length; i++) 
            {
                if(i==saltaIndices)
                    continue;
                
                String titulo;
                String valor;
                try
                {
                    titulo = datos[i][0];
                    valor = datos[i][1];
                }
                catch(NullPointerException e)
                {
                    return false;
                }
                
                Element element = doc.createElement(titulo);
                Text text = doc.createTextNode(valor);
                element.appendChild(text);
                elePadre.appendChild(element);
            }
            raiz.appendChild(elePadre);
            return true;
        }
        else
            return false;
    }
    /**
     * Crea un hijo de raiz con los datos del array.
     * @param datos array que contiene los datos del nodo.
     * Tiene que ser en el formato String[n campos][2],
     * donde en la fila se guardan los campos y en las columnas los valores.
     * @param saltaIndices indice de la lista de datos que no queremos agregar.
     * @return true en caso de que se logre crear el nodo con exito.
     */
    public boolean creaNodo(String[][] datos, int saltaIndices)
    {        
        String tituloNodo=hijosRaiz[0].getNodeName();

        if(datos[0].length==2)
        {
            Element elePadre = doc.createElement(tituloNodo);
            
            for(int i = 0; i < datos.length; i++) 
            {
                if(i==saltaIndices)
                    continue;
                
                String titulo;
                String valor;
                try
                {
                    titulo = datos[i][0];
                    valor = datos[i][1];
                }
                catch(NullPointerException e)
                {
                    return false;
                }
                
                Element element = doc.createElement(titulo);
                Text text = doc.createTextNode(valor);
                element.appendChild(text);
                elePadre.appendChild(element);
            }
            raiz.appendChild(elePadre);
            return true;
        }
        else
            return false;
    }
    /**
     * Crea un hijo de raiz con los datos del array.
     * @param datos array que contiene los datos del nodo.
     * Tiene que ser en el formato String[n campos][2],
     * donde en la fila se guardan los campos y en las columnas los valores.
     * @param saltaIndices lista con los indices que tiene que saltarse 
     * en el array de datos[][] (estos no seran agregados al xml).
     * @return true en caso de que se logre crear el nodo con exito.
     */
    public boolean creaNodo(String[][] datos, int []saltaIndices)
    {
        String tituloNodo=hijosRaiz[0].getNodeName();
        
        if(datos[0].length==2)
        {
            Element elePadre = doc.createElement(tituloNodo);
            
            for(int i = 0; i < datos.length; i++) 
            {
                if(compruebaNum(i, saltaIndices))
                    continue;
                
                String titulo;
                String valor;
                try
                {
                    titulo = datos[i][0];
                    valor = datos[i][1];
                }
                catch(NullPointerException e)
                {
                    return false;
                }
                
                Element element = doc.createElement(titulo);
                Text text = doc.createTextNode(valor);
                element.appendChild(text);
                elePadre.appendChild(element);
            }
            raiz.appendChild(elePadre);
            return true;
        }
        else
            return false;
    }
    /**
     * Crea un hijo de raiz con los datos del array.
     * @param datos array que contiene los datos del nodo.
     * Tiene que ser en el formato String[n campos][2],
     * donde en la fila se guardan los campos y en las columnas los valores.
     * @return true en caso de que se logre crear el nodo con exito.
     */
    public boolean creaNodo(String[][] datos)
    {
        String tituloNodo=hijosRaiz[0].getNodeName();
        
        if(datos[0].length==2)
        {
            Element elePadre = doc.createElement(tituloNodo);
            
            for(int i = 0; i < datos.length; i++) 
            {
                String titulo=null;
                String valor=null;
                try
                {
                    titulo = datos[i][0];
                    valor = datos[i][1];
                }
                catch(NullPointerException e)
                {
                    return false;
                }
                
                Element element = doc.createElement(titulo);
                Text text = doc.createTextNode(valor);
                element.appendChild(text);
                elePadre.appendChild(element);
            }
            raiz.appendChild(elePadre);
            return true;
        }
        else
            return false;
    }
    /**
     * Devuelve el indice en el array de hijosRaiz del nodo buscado.
     * @param campo Campo para buscar el nodo.
     * @param valorCampo valor que tiene que tener el campo buscado.
     * @return indice del nodo.
     */
    public int getIndiceNodo(String campo, String valorCampo)
    {
       Node node;
       
       for (int i=0; i<hijosRaiz.length; i++)
       {
            node = hijosRaiz[i];
            //Es un nodo libro que hay que procesar si es de tipo Elemento
            String valorCampoNodo = GestionaXML.this.getValorCampo(node, campo);
            if(valorCampo.equals(valorCampoNodo))
                return i;
       }
        return -1;
    }
    /**
     * Devuelve el indice del primer nodo en el que coincidan 
     * todos los valores en los campos a buscar (estricto==true) o en el primero
     * que coincida alguno (estricto==false).
     * @param campos Array de Strings con los nombres de los campos a buscar.
     * @param valores Array de Strings con los valores de los campos a buscar.
     * @param estricto si es true tienen que coincidar todos los campos, sino solo tiene que coincidir alguno.
     * @return el indice del primer nodo en el que coincidan 
     * todos los valores en los campos a buscar (estricto==true) o en el primero
     * que coincida alguno (estricto==false).
     */
    public int getIndiceNodo(String campos[], String valores[], boolean estricto)
    {
        Node node;
        int numCoincidencias;
        
        if(campos.length == valores.length)
        {
            for (int i=0; i<hijosRaiz.length; i++)
            {
                numCoincidencias=0;
                node = hijosRaiz[i];

                for (int j = 0; j < campos.length; j++) 
                {
                   String valor = valores[j];
                   String campo = campos[j];
                   String valorCampo = GestionaXML.this.getValorCampo(node, campo);

                   if(valorCampo.equals(valor))
                       numCoincidencias++;
               }
                
                if(numCoincidencias==campos.length)
                    return i;
                if(numCoincidencias>0 && !estricto)
                    return i;
            }
        }
        return -1;
    }
    /**
     * Devuelve todos los indices en el array de hijosRaiz del nodo buscado.
     * @param campo Campo para buscar el nodo.
     * @param valorCampo valor que tiene que tener el campo buscado.
     * @return indice del nodo.
     */
    public int[] getIndicesNodos(String campo, String valorCampo)
    {
        List listaIndices = new List();
        Node node;

        for (int i=0; i<hijosRaiz.length; i++)
        {
             node = hijosRaiz[i];
             //Es un nodo libro que hay que procesar si es de tipo Elemento
             String valorCampoNodo = GestionaXML.this.getValorCampo(node, campo);
             if(valorCampo.equals(valorCampoNodo))
                 listaIndices.add(String.valueOf(i));
        }
        
        String[] sIndices = listaIndices.getItems();
        int indices[] = new int[sIndices.length];
        for (int i = 0; i < sIndices.length; i++)
            indices[i] = Integer.valueOf(sIndices[i]);
        
        return indices;
    }
    /**
     * Devuelve el indice del primer nodo en el que coincidan 
     * todos los valores en los campos a buscar (estricto==true) o en el primero
     * que coincida alguno (estricto==false).
     * @param campos Array de Strings con los nombres de los campos a buscar.
     * @param valores Array de Strings con los valores de los campos a buscar.
     * @param estricto si es true tienen que coincidar todos los campos, sino solo tiene que coincidir alguno.
     * @return el indice del primer nodo en el que coincidan 
     * todos los valores en los campos a buscar (estricto==true) o en el primero
     * que coincida alguno (estricto==false).
     */
    public int[] getIndicesNodos(String campos[], String valores[], boolean estricto)
    {
        Node node;
        int numCoincidencias;
        List listaIndices = new List();
        
        if(campos.length == valores.length)
        {
            for (int i=0; i<hijosRaiz.length; i++)
            {
                numCoincidencias=0;
                node = hijosRaiz[i];

                for (int j = 0; j < campos.length; j++) 
                {
                   String valor = valores[j];
                   String campo = campos[j];
                   String valorCampo = GestionaXML.this.getValorCampo(node, campo);

                   if(valorCampo.equals(valor))
                       numCoincidencias++;
               }
                
                if(numCoincidencias==campos.length)
                    listaIndices.add(String.valueOf(i));
                else if(numCoincidencias>0 && !estricto)
                    listaIndices.add(String.valueOf(i));
            }
        }
        
        String[] sIndices = listaIndices.getItems();
        int indices[] = new int[sIndices.length];
        for (int i = 0; i < sIndices.length; i++)
            indices[i] = Integer.valueOf(sIndices[i]);
        
        return indices;
    }
    /**
     * Devuelve los indices de todos los nodos tipo Elemenet en el archivo.
     * @return array con todos los indices de nodos Element.
     */
    public int[] getIndicesNodos()
    {
        List listaIndices = new List();
        
        for (int i = 0; i < hijosRaiz.length; i++) {
            Node node = hijosRaiz[i];
            if(node.getNodeType()==Node.ELEMENT_NODE)
                listaIndices.add(String.valueOf(i));
        }
        
        int[] indices = new int[listaIndices.getItemCount()];
        for (int i = 0; i < indices.length; i++)
            indices[i]=Integer.valueOf(listaIndices.getItem(i));
        
        return indices;
    }
    /**
     * Modifica la informacion del campo de un nodo.
     * @param indiceNodo indice del nodo a cambiar.
     * @param campoCambiar Titulo del campo a buscar en el nodo.
     * @param nuevoDato Nuevo valor del campo.
     * @return true o false en funcion de si se ha conseguido modificar la info.
     */
    public boolean cambiaInfo(String campoCambiar, String nuevoDato, int indiceNodo) 
    {
        NodeList hijos;
        Node ntemp;
        
        try
        {
            hijos = hijosRaiz[indiceNodo].getChildNodes();
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return false;
        }
        
        for (int i=0; i<hijos.getLength(); i++)
        {
            ntemp = hijos.item(i);
            if(ntemp.getNodeType()==Node.ELEMENT_NODE)
            {
                String tituloInfo=ntemp.getNodeName();
                if(tituloInfo.equals(campoCambiar))
                {
                    ntemp.getChildNodes().item(0).setNodeValue(nuevoDato);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Vuelve a cargar los nodos en hijosRaiz.
     */
    public void recargaNodosRaiz()
    {
        hijosRaiz=getNodosRaiz();
    }
    /**
     * Devuelve la ruta y nodo raiz del fichero.
     * @return String con la ruta y el nodo raiz del fichero unidos por un "-".
     */
    @Override
    public String toString() {
        return this.fichero.getPath()+"-"+raiz.getNodeName();
    }
}