public class Utils {
    public static double productoPunto(double[] vector1, double[] vector2) {
        // Asegurarse de que ambos vectores tengan la misma longitud
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Los vectores deben tener la misma longitud");
        }

        double productoPunto = 0;
        // Calcular el producto punto sumando los productos de los elementos correspondientes
        for (int i = 0; i < vector1.length; i++) {
            productoPunto += vector1[i] * vector2[i];
        }

        return productoPunto;
    }
}
