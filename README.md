# HEMISPHERE Project.

All the dependent libraries are included locally in the package, in the thirdpary directory. 
This is to make sure we can distribute the code easily. However, it means we have to keep an eye 
on any update of the libraries. 

In the current version, they are:
 
- core.jar        PROCESSING core functionalities
- arcball.jar     PROCESSING library for track ball interface
                (http://processing.org/hacks/doku.php?id=hacks:arcball)
- noc.jar         PROCESSING library for 3D vector manipulation
                (http://www.shiffman.net/teaching/the-nature-of-code/library/)
- surface.jar     PROCESSING library for creating 3D surfaces
                (http://www.eskimoblood.de/surfacelib/)
- wiiusej.jar     JAVA library for the Wii controler
                (http://code.google.com/p/wiiusej/)

Note that the wiiusej comes with two Windows DLLs, wiiuse.dll and WiiUseJ.dll. 
Both HAVE TO BE LOCATED at the root of the NetBeans project. 
If you build a stand-alone distribution (JAR file) of the application, NetBeans will put everything 
in the dist directory, BUT NOT THE two dlls. 
MAKE SURE TO COPY THEM BY HAND BEFORE distributing the package (will do it through ant at some point in the future).

