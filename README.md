# GestionaXML
Esta es una clase para gestionar ficheros XML (Funciona solo a un nivel de profundidad).

Ej fichero correcto:
<xmp>
 <Productos>
   <Producto>
       <Nombre>Peonias</Nombre>
       <Precio>4</Precio>
       <Icono>peonias.png</Icono>
   </Producto>
   <Producto>
       <Nombre>Rosas</Nombre>
       <Precio>2</Precio>
       <Icono>rosas.png</Icono>
   </Producto>
  </Productos>
 </xmp>
 
 
Ej fichero incorrecto:
 
 <xmp>
 <Productos>
   <Producto>
       <flor>
           <Nombre>Peonias</Nombre>
           <Precio>4</Precio>
           <Icono>peonias.png</Icono>
       </flor>
   </Producto>
   <Producto>
       <flor>
           <Nombre>Rosas</Nombre>
           <Precio>2</Precio>
           <Icono>rosas.png</Icono>
       </flor>
   </Producto>
  </Productos>
