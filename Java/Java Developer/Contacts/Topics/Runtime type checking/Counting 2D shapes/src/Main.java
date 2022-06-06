class Counter {
    public static int count2DShapes(Shape[] shapes) {
        return (int) Arrays.stream(shapes).filter(shape -> Shape2D.class.isInstance(shape) && shape.getClass() != Shape2D.class).count();
    }
}

// Don't change the code below

class Shape {
}

class Shape2D extends Shape {
}

class Shape3D extends Shape {
}


class Circle extends Shape2D {
}

class Shape2DSub1 extends Shape2D {
}

class Shape2DSub2 extends Shape2D {
}


class Cube extends Shape3D {
}

class Shape3DSub1 extends Shape3D {
}

class Shape3DSub2 extends Shape3D {
}