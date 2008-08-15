/*
 * SphericalSrc.java
 *
 * Created on 08 July 2008, 16:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hemisphere;
/***********************************************************************\
* spherical.java (Spherical Triangle Demo Applet), by Jim Arvo.         *
*                                                                       *
*               California Institute of Technology                      *
*                     arvo@cs.caltech.edu                               *
*                 http://www.cs.caltech.edu/~arvo                       *
*_______________________________________________________________________*
*                                                                       *
* This applet demonstrates an algorithm for generating random samples   *
* from arbitrary spherical triangles, as described in the paper         *
*                                                                       *
*     "Stratified Sampling of Spherical Triangles",                     *
*     Computer Graphics Proceedings, Annual Conference Series,          *
*     ACM SIGGRAPH `95, pages 437-438, 1995.                            *
*                                                                       *
* The program allows the user to interactively change a spherical       *
* triangle by picking and dragging its vertices.  When the "Resample"   *
* button is pressed, a set of random numbers is generated and mapped    *
* to the sphere.  These random numbers remain fixed as the triangle is  *
* altered.  The samples within the triangle can be either "Stratified"  *
* or "Uniform".  Both sampling methods use the same set of random       *
* numbers and the same mapping to the sphere; the only difference is    *
* that stratification requires the random number pairs be re-mapped     *
* to the cells of a uniform grid before being mapped to the sphere.     *
*                                                                       *
*                                                                       *
*  Changes:                                                             *
*                                                                       *
*      02/07/96    arvo    Original coding.                             *
*      17/12/01    knill   Change colors, names of classes              *
*                          to fit demo pages                            *
\***********************************************************************/
import java.awt.*;
import java.applet.*;
import java.util.*;

public class SphericalSrc extends Applet 
    {
    public void init() 
        {
	setLayout( new BorderLayout() );
	spherical_panel s = new spherical_panel();
	add( "Center", s );
	add( "South" , new spherical_controls( s ) );
        }

    public boolean handleEvent( Event e ) 
        {
        if( e.id == Event.WINDOW_DESTROY )
            {
            System.exit(0);
            return true;
            }
        return false;
        }

    public static void main( String args[] ) 
        {
	Frame      f = new Frame( "Spherical Triangle" );
	SphericalSrc s = new SphericalSrc();
	s.init();
	s.start();
	f.add( "Center", s );
	f.resize( 300, 300 );
	f.show();
        }
    }



class vec2i
    {
    public int x, y;
    public vec2i( )                 { set( 0, 0 );  }
    public vec2i( int a, int b )    { set( a, b );  }
    public void set( int a, int b ) { x = a; y = b; }
    }


class vec2
    {
    public float x, y;
    public vec2( ) { set( 0, 0 ); }
    public vec2( float  a, float  b ) { set( a, b ); }
    public vec2( double a, double b ) { set( a, b ); }
    public void set( float a, float b ) { x = a; y = b; }
    public void set( double a, double b ) { x = (float)a; y = (float)b; }
    }

class vec3
    {
    public static vec3 Null  = new vec3( 0, 0, 0 );
    public static vec3 Xaxis = new vec3( 1, 0, 0 );
    public static vec3 Yaxis = new vec3( 0, 1, 0 );
    public static vec3 Zaxis = new vec3( 0, 0, 1 );

    public float x, y, z;
    public vec3( )
        {
        set( 0, 0, 0 );
        }
    public vec3( float a, float b, float c )
        {
        set( a, b, c );
        }
    public float len2()
        {
        return x * x + y * y + z * z;
        }
    public float len()
        {
        return (float)Math.sqrt( x * x + y * y + z * z );
        }
    public float dot( vec3 A )
        {
        return x * A.x + y * A.y + z * A.z;
        }
    public void set( float a, float b, float c )
        {
        x = a;
        y = b;
        z = c;
        }
    public void set( double a, double b, double c )
        {
        x = (float)a;
        y = (float)b;
        z = (float)c;
        }
    public void set( vec3 v )
        {
        set( v.x, v.y, v.z );
        }
    public void set( float a, vec3 A, float b, vec3 B )
        {
        x = a * A.x + b * B.x;
        y = a * A.y + b * B.y;
        z = a * A.z + b * B.z;
        }
    public void remove( vec3 A )  // Remove component parallel to A.
        {
        float tmp = ( x * A.x + y * A.y + z * A.z ) / A.len2();
        x -= tmp * A.x;
        y -= tmp * A.y; 
        z -= tmp * A.z;
        }
    public void add( vec3 A )
        {
        x += A.x;
        y += A.y;
        z += A.z;
        }
    public void Normalize()
        {
        double c = 1.0 / Math.sqrt( x*x + y*y + z*z );
        x = (float)( x * c );
        y = (float)( y * c );
        z = (float)( z * c );
        }
    public void cross( vec3 A, vec3 B )
        {
        x = A.y * B.z - A.z * B.y;
        y = A.z * B.x - A.x * B.z;
        z = A.x * B.y - A.y * B.x;
        }
    public void flip()
        {
        x = -x;
        y = -y;
        z = -z;
        }
    public float tripleProduct( vec3 B, vec3 C )
        {
        return x * ( B.y * C.z - B.z * C.y ) + 
               y * ( B.z * C.x - B.x * C.z ) +
               z * ( B.x * C.y - B.y * C.x ) ;
        }
    }

class unit3 extends vec3
    {
    public unit3( )
        {
        super.set( 0, 0, 1 );
        }
    public unit3( float a, float b, float c )
        {
        this.set( a, b, c );
        }
    public unit3( double a, double b, double c )
        {
        this.set( a, b, c );
        }
    public void set( float a, float b, float c )
        {
        double len = Math.sqrt( a*a + b*b + c*c );
        x = (float)( a / len );
        y = (float)( b / len );
        z = (float)( c / len );
        }
    public void set( double a, double b, double c )
        {
        double len = Math.sqrt( a*a + b*b + c*c );
        x = (float)( a / len );
        y = (float)( b / len );
        z = (float)( c / len );
        }
    public void set( float a, vec3 A, float b, vec3 B )
        {
        super.set( a, A, b, B );
        Normalize();
        }
    public void set( vec3 v )
        {
        this.set( v.x, v.y, v.z );
        }
    public void set( unit3 u )
        {
        super.set( u );
        }
    public void remove( vec3 A )
        {
        float c = x * A.x + y * A.y + z * A.z;  // No need to normalize.
        x -= c * A.x;
        y -= c * A.y; 
        z -= c * A.z;
        Normalize();
        }
    public void cross( vec3 A, vec3 B )
        {
        super.cross( A, B );
        Normalize();
        }
    public void add( vec3 A )
        {
        super.add( A );
        Normalize();
        }
    }



/***********************************************************************\
*                                                                       *
* This class handles the graphical output and the interaction with      *
* the user (picking and dragging).                                      *
*                                                                       *
\***********************************************************************/
class spherical_panel extends Panel 
    {
    int      Tol    = 6,
             MaxPts = 100,
             npts   = 10,
             picked = 0,
             border,
             radius,
             diameter;

    boolean  show_pts    = false,
             do_uniform  = false,
             do_strat    = true,
             do_sphere   = false,
             do_triangle = true;

    Vector   points = new Vector(),
             coords = new Vector();

    vec2     r = new vec2();

    vec2i    p = new vec2i(),
             q = new vec2i(),
             center = new vec2i();

    unit3    A = new unit3( -0.570, -0.58, 0.57 ),
             B = new unit3( -0.068,  0.77, 0.63 ),
             C = new unit3(  0.910,  0.18, 0.35 ),
             P = new unit3(),
             M = new unit3(),
             Q = new unit3(),
             u = new unit3();

    Random   rng = new Random( 123456 );
    spherical_trigonometry   S   = new spherical_trigonometry( A, B, C );
    Graphics G;


    public spherical_panel() 
        {
	setBackground( null );
	setForeground( Color.black );
        for( int i = 0; i < MaxPts; i++ )
            {
            coords.addElement( new vec2() );
            points.addElement( new unit3()  );
            }
        Resample();
        }

    public void SampleSphere()
        {
        do_sphere   = true;
        do_triangle = false;
        repaint();
        }

    public void SampleTriangle()
        {
        do_sphere   = false;
        do_triangle = true;
        repaint();
        }

    public void Stratified() 
        {
        do_strat   = true;
        do_uniform = false;
        repaint();
        }

    public void Uniform() 
        {
        do_strat   = false;
        do_uniform = true;
        repaint();
        }

    public void Resample()
        {
        int k = 0;
        show_pts = true;
        for( int i = 0; i < npts; i++ )
        for( int j = 0; j < npts; j++ )
            {
            float x = rng.nextFloat();
            float y = rng.nextFloat();
            ( (vec2)coords.elementAt(k++) ).set( x, y );
            }        
        repaint();
        }

    public void Clear()  // Hide all the sample points.
        {
        show_pts = false;
        repaint();
        }

    private void drawPoints()
        {
        if( !show_pts ) return;
        G.setColor( Color.black );
        int k = 0;
	int np = coords.size();
        float si = (float)( 1.0 / npts );
        float sj = (float)( 1.0 / npts );
        for( int i = 0; i < npts; i++ )
        for( int j = 0; j < npts; j++ )
            {
            r = (vec2)coords.elementAt( k++ );
            if( do_sphere && do_uniform )
                {
                float theta = (float)( r.x * 3.14159 * 2.0 );
                float z     = (float)( 2.0 * r.y - 1.0 );
                float w     = (float)Math.sqrt( 1.0 - z*z );
                u.set( w * Math.cos( theta ), w * Math.sin( theta ), z );
                }
            if( do_sphere && do_strat )
                {
                float theta = (float)( ( i + r.x ) * si * 3.14159 * 2.0 );
                float z     = (float)( 2.0 * ( j + r.y ) * sj - 1.0 );
                float w     = (float)Math.sqrt( 1.0 - z*z );
                u.set( w * Math.cos( theta ), w * Math.sin( theta ), z );
                }
            else if( do_triangle && do_uniform )
                {
                u.set( S.Sample( r.x, r.y ) );
                }
            else if( do_triangle && do_strat )
                {
                u.set( S.Sample( ( i + r.x ) * si, ( j + r.y ) * sj ) );
                }

            int x = center.x + (int)( u.x * radius );
            int y = center.y + (int)( u.y * radius );

            G.drawLine( x - 2, y, x + 2, y );
            G.drawLine( x, y - 2, x, y + 2 );
            }        
        }

    private void markVertex( unit3 A, Color c, int rad )
        {
        int x = center.x + (int)( A.x * radius );
        int y = center.y + (int)( A.y * radius );
        G.setColor( c );
        G.fillOval( x - rad, y - rad, 2 * rad, 2 * rad );
        }

    private void markVertices()
        {
        if( !do_triangle ) return;
        markVertex( A, Color.blue, 3 );
        markVertex( B, Color.blue, 3 );
        markVertex( C, Color.blue, 3 );
        if( picked == 1 ) markVertex( A, Color.red, 6 );
        if( picked == 2 ) markVertex( B, Color.red, 6 );
        if( picked == 3 ) markVertex( C, Color.red, 6 );
        }

    private boolean pickPoint( unit3 P, int x, int y )
        {
        q = coord( P );
        if( Math.abs( x - q.x ) > Tol ) return false;
        if( Math.abs( y - q.y ) > Tol ) return false;
        return true;
        }

    private int pick( int x, int y )
        {
        if( !do_triangle ) return 0;
        if( pickPoint( A, x, y ) ) return 1;
        if( pickPoint( B, x, y ) ) return 2;
        if( pickPoint( C, x, y ) ) return 3;
        return 0;
        }

    private vec2i coord( unit3 P )
        {
        p.set( center.x + (int)( P.x * radius ),
               center.y + (int)( P.y * radius ) );
        return p;
        }

    private unit3 toSphere( int x, int y )
        {
        float s  = 1 / (float)radius;
        float rx = s * ( x - center.x );
        float ry = s * ( y - center.y );
        float r2 = (float)Math.min( rx * rx + ry * ry, 1.0 );
        u.set( rx, ry, (float)Math.sqrt( 1.0 - r2 ) );
        return u;
        }

    private void drawLine( float x1, float y1, float x2, float y2 )
        {
        G.drawLine( 
            center.x + (int)( x1 * radius ),
            center.y + (int)( y1 * radius ),
            center.x + (int)( x2 * radius ),
            center.y + (int)( y2 * radius )
            );
        }

    private void drawArc( unit3 A, unit3 B )  // Connect points on the sphere.
        {
        int steps = 10;
        float c = (float)( 1.0 / steps );
        M.set( A.x + B.x, A.y + B.y, A.z + B.z );
        float dx = c * ( M.x - A.x );
        float dy = c * ( M.y - A.y );
        float dz = c * ( M.z - A.z );
        P.set( A );
        for( int i = 1; i <= steps; i++ )  // Draw the first half.
            {
            Q.set( A.x + i * dx, A.y + i * dy, A.z + i * dz );
            drawLine( P.x, P.y, Q.x, Q.y );
            P.x = Q.x;
            P.y = Q.y;
            }
        dx = c * ( B.x - M.x );
        dy = c * ( B.y - M.y );
        dz = c * ( B.z - M.z );
        P.set( M );
        for( int i = 1; i <= steps; i++ )  // Draw the second half.
            {
            Q.set( M.x + i * dx, M.y + i * dy, M.z + i * dz );
            drawLine( P.x, P.y, Q.x, Q.y );
            P.x = Q.x;
            P.y = Q.y;
            }
        }

    private void drawTriangle() 
        {
        if( !do_triangle ) return;
        G.setColor( Color.blue );
        drawArc( A, B );
        drawArc( B, C );
        drawArc( C, A );
        }

    private void drawSphere() 
        {
        G.setColor( Color.black );
        G.drawOval( 
            center.x - radius, 
            center.y - radius, 
            diameter,
            diameter
            );
        }

    public boolean handleEvent( Event e ) 
        {
	switch( e.id ) 
            {
	    case Event.MOUSE_DOWN:
                picked = pick( e.x, e.y );
                if( picked > 0 ) repaint();
                return true;

	    case Event.MOUSE_UP:
                if( picked > 0 ) repaint();
                picked = 0;
                return true;

            case Event.MOUSE_DRAG:
                if( picked == 1 ) A.set( toSphere( e.x, e.y ) );
                if( picked == 2 ) B.set( toSphere( e.x, e.y ) );
                if( picked == 3 ) C.set( toSphere( e.x, e.y ) );
                if( picked >  0 )
                    {
                    S.set( A, B, C );
                    repaint();
                    return true;
                    }
                return true;

            case Event.WINDOW_DESTROY:
                System.exit(0);
                return true;

            default: return false;
            }
        }

    public void start() 
        {
        setBackground( Color.white );
	Rectangle R = bounds();
        border   = 10;
        center.x = R.width  / 2;
        center.y = R.height / 2;
        radius   = Math.min( R.width, R.height ) / 2 - border;
        diameter = 2 * radius;
        }

    public void paint( Graphics g ) 
        {
        start();
        G = g;
	G.setColor( Color.black );
	G.setPaintMode();
        drawSphere();
        markVertices();
        drawTriangle();
        drawPoints();
        }
    }


/***********************************************************************\
*                                                                       *
* This class creates the user interface and handles button events.      *
*                                                                       *
\***********************************************************************/
class spherical_controls extends Panel 
    {
    spherical_panel panel;
    Checkbox strt, unif;
    Checkbox sph, tri;
    Button   resamp, clear;

    public spherical_controls( spherical_panel panel ) 
        {
	this.panel = panel;
	setLayout( new FlowLayout() );

	CheckboxGroup group1 = new CheckboxGroup();
	add( sph = new Checkbox( "Sphere"  , group1, false ) );
	add( tri = new Checkbox( "Triangle", group1, true  ) );

	CheckboxGroup group2 = new CheckboxGroup();
	add( unif = new Checkbox( "Uniform"   , group2, false ) );
	add( strt = new Checkbox( "Stratified", group2, true  ) );

        add( resamp = new Button( "Resample" ) );
        add( clear  = new Button( "Clear   " ) );

        sph  .setBackground( null );
        tri  .setBackground( null );
        strt .setBackground( null );
        unif .setBackground( null );
	panel.setForeground( null );
        }

    public void paint( Graphics g )  
        {
	setBackground( Color.white );
	Rectangle R = bounds();
	g.setColor( Color.white );
	g.draw3DRect( 1, 1, R.width - 3, R.height - 3, false );
	g.setColor( Color.black );
	g.draw3DRect( 0, 0, R.width - 1, R.height - 1, false );
        }

    public boolean action( Event e, Object arg ) 
        {
	if( e.target instanceof Button ) 
            {
            if( e.target == resamp ) panel.Resample();
            if( e.target == clear  ) panel.Clear(); 
            return true;
            }
        if( e.target instanceof Checkbox )
            {
            String lab = ( (Checkbox)e.target ).getLabel();
            if( lab.equals( "Uniform"    ) ) panel.Uniform();
            if( lab.equals( "Stratified" ) ) panel.Stratified();
            if( lab.equals( "Sphere"     ) ) panel.SampleSphere();
            if( lab.equals( "Triangle"   ) ) panel.SampleTriangle();
            return true;
            }
        return false;
        }
    }



/**
 * \
 * SphericalSrc Triangle class  (spherical_trigonometry)                    *
 *                                                                       *
 * The SphericalSrc Triangle ABC.  Edge lengths (segments of great          *
 * circles) are a, b, and c.  The internal angles (dihedral angles       *
 * between the planes containing the edges) are Alpha, Beta, and Gamma.  *
 *                                                                       *
 *         B                                                             *
 *           o                                                           *
 *           | \                                                         *
 *           |   \                                                       *
 *           |beta \                                                     *
 *           |       \                                                   *
 *           |         \ a                                               *
 *         c |           \                                               *
 *           |             \                                             *
 *           |               \                                           *
 *           |                 \                                         *
 *           |                   \                                       *
 *           |alpha         gamma  \                                     *
 *           o----------------------o                                    *
 *         A            b              C                                 *
 *                                                                       *
 *                                                                       *
 * \**********************************************************************
 */
class spherical_trigonometry 
    {
    // Define the nine basic entities associated with a spherical 
    // triangle: 3 vertices, 3 edge lenghts, and 3 angles.

    private unit3 A = new unit3();
    private unit3 B = new unit3();
    private unit3 C = new unit3();
    private float a, b, c;
    private float alpha, beta, gamma;

    // Define numerous temporary variables that are used in
    // sampling from a spherical triangle.

    private unit3 P  = new unit3();
    private unit3 U  = new unit3();
    private unit3 T  = new unit3();
    private unit3 C2 = new unit3();
    private unit3 U1 = new unit3();
    private unit3 U2 = new unit3();
    private vec3  V1 = new vec3();
    private vec3  V2 = new vec3();
    private vec2  q  = new vec2();
    private float cos_a, cos_b, cos_c;
    private float cos_alpha, cos_beta, cos_gamma;
    private float area;
    private float sin_alpha, product;  // Used in sampling algorithm.
    private int   orient;
    private float Pi = (float)3.1415926;

    spherical_trigonometry() 
        { 
        set( vec3.Xaxis, vec3.Yaxis, vec3.Zaxis ); 
        }

    spherical_trigonometry( vec3 A0, vec3 B0, vec3 C0 )
        { 
        set( A0, B0, C0 ); 
        }

    spherical_trigonometry( spherical_trigonometry T ) 
        { 
        set( T.A, T.B, T.C ); 
        }

    public void set( vec3 A0, vec3 B0, vec3 C0 )
        {
        // Normalize the three vectors that define the vertices.

        A.set( A0 );
        B.set( B0 );
        C.set( C0 );

        // Compute and save the cosines of the edge lengths.

        cos_a = B.dot( C );
        cos_b = A.dot( C );
        cos_c = A.dot( B );

        // Compute and save the edge lengths.
 
        a = (float)Math.acos( cos_a );
        b = (float)Math.acos( cos_b );
        c = (float)Math.acos( cos_c );

        // Compute the cosines of the internal (i.e. dihedral) angles.

        cos_alpha = CosDihedralAngle( C, A, B );
        cos_beta  = CosDihedralAngle( A, B, C );
        cos_gamma = CosDihedralAngle( A, C, B );

        // Compute the (dihedral) angles.

        alpha = (float)Math.acos( cos_alpha );
        beta  = (float)Math.acos( cos_beta  );
        gamma = (float)Math.acos( cos_gamma );

        // Compute the solid angle of the spherical triangle.

        area = alpha + beta + gamma - Pi;

        // Compute the orientation of the triangle.

        if( A.tripleProduct( B, C ) > 0 ) orient = 1; 
        else orient = -1;

        // Initialize three variables that are used for sampling the triangle.

        U.set( C );
        U.remove( A );  // In plane of AC orthogonal to A.
        sin_alpha = (float)Math.sin( alpha );
        product   = sin_alpha * cos_c;
        }

    public boolean Inside( vec3 W )
        {
        if( orient * W.tripleProduct( A, B ) < 0.0 ) return false;
        if( orient * W.tripleProduct( B, C ) < 0.0 ) return false;
        if( orient * W.tripleProduct( C, A ) < 0.0 ) return false;
        return true;
        }

    public float CosDihedralAngle( vec3 A, vec3 B, vec3 C )
        {
        U1.cross( A, B );
        U2.cross( C, B );
        return U1.dot( U2 );
        }

    public float DihedralAngle( vec3 A, vec3 B, vec3 C )
        {
        return (float)Math.acos( CosDihedralAngle( A, B, C ) );
        }

    public unit3 Sample( float x1, float x2 )  // Map (x1,x2) to the triangle.
        {
        // Use one random variable to select the area of the sub-triangle.
        // Save the sine and cosine of the angle phi.

        float phi = x1 * area - alpha;
        float s   = (float)Math.sin( phi );
        float t   = (float)Math.cos( phi );

        // Compute the pair (u,v) that determines the new angle beta.

        float u = t - cos_alpha;
        float v = s + product  ;

        // Compute the cosine of the new edge b.

        float q = ( cos_alpha * ( v * t - u * s ) - v ) / 
                  ( sin_alpha * ( u * t + v * s )     );

        // Compute the third vertex of the sub-triangle.

        double temp = Math.max( 1.0 - q * q, 0.0 );
        T.set( q, A, (float)Math.sqrt( temp ), U );

        // Use the other random variable to select the height z.

        float z = (float)( 1.0 - x2 * ( 1.0 - T.dot( B ) ) );

        // Construct the corresponding point on the sphere.

        T.remove( B );  // Remove B component of T.
        P.set( z, B, (float)Math.sqrt( 1.0 - z * z ), T );
        return P;
        }

    public vec2 Coord( unit3 W )  // This is the inverse of "Sample".
        {
        // Compute the new C vertex, which lies on the arc defined by B-W
        // and the arc defined by A-C.

        V1.cross( B , W  );
        V2.cross( C , A  );
        C2.cross( V1, V2 );

        // Adjust the sign of C2.  Make sure it's on the arc between A and C.

        V1.set( A );
        V1.add( C );
        if( C2.dot( V1 ) < 0.0 ) C2.flip();

        // Compute x1, the ratio of the areas, sub-triangle to original.

        float cos_beta  = CosDihedralAngle( A, B , C2 );
        float cos_gamma = CosDihedralAngle( A, C2, B  );
        float sub_area  = (float)( 
            alpha - Pi + Math.acos( cos_beta  ) + Math.acos( cos_gamma ) );
        float x1 = sub_area / area;

        // Now compute the second coordinate using the new C vertex.

        float z  = W.dot( B );
        float x2 = (float)( ( 1.0 - z ) / ( 1.0 - C2.dot( B ) ) );

        q.set( clamp( x1 ), clamp( x2 ) );
        return q;
        }

    private float clamp( float x )
        {
        if( x < 0 ) return 0;
        if( x > 1 ) return 1;
        return x;
        }

    }

