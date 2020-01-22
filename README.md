# GestionaXML
Esta es una clase para gestionar ficheros XML (Funciona solo a un nivel de profundidad).

Ej fichero correcto:
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
 
Ej fichero incorrecto:
 
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
