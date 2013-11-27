## This project generates a java inline code for a static array 
## of colors that are in some sense alike and good.
## Author: Jon Arnar

def hsvToRgb(hue,sat,value):
        c = sat*value
        h = hue*6
        x = c*(1-abs((h%2)-1))

        permutations = [[c,x,0],[x,c,0],[0,c,x],[0,x,c],[x,0,c],[c,0,x]]


        r = permutations[int(h)][0]
        g = permutations[int(h)][1]
        b = permutations[int(h)][2]

        m = value - c

        return [int(255*(r+m)),int(255*(g+m)),int(255*(b+m))]




if __name__ == '__main__':
        n = 100
        golden_ratio_conjugate = 0.618033988749895
        h = 0
        s = 0.95
        v = 0.95
        javaArray = "public static String[] colors = new String[]{\n"
        for i in range(0, n):
                rgb = hsvToRgb(h,s,v)
                javaArray += "\"rgb(%i,%i,%i)\",\n" % tuple(rgb)
                h += golden_ratio_conjugate
                h %= 1

        
        javaArray = javaArray[0:-2]
        javaArray +="};\n"
        print javaArray
