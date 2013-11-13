def hsvToRgb(hue,sat,value):
        c = sat*value
        h = hue*6
        x = c*(1-abs((h%2)-1))
        
        r = 0
        g = 0
        b = 0

        if(0 <= h and h < 1):
                r = c
                g = b
        elif(1 <= h and h < 2):
                r = x
                g = c
        elif(2 <= h and h < 3):
                g = c
                b = x
        elif(3 <= h and h < 4):
                g = x
                b = c
        elif(4 <= h and h < 5):
                r = x
                b = c
        elif(5 <= h and h < 6):
                r = c
                b = x
        m = value - c

        return [int(255*(r+m)),int(255*(g+m)),int(255*(b+m))]




if __name__ == '__main__':
        n = 100
        golden_ratio_conjugate = 0.618033988749895
        h = 0
        s = 0.5
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
