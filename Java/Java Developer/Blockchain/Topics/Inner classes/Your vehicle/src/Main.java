class EnjoyVehicle {

    public static void startVehicle() {
        // start your vehicle
        Vehicle vehicle = new Vehicle();
        Vehicle.Engine engine = vehicle.new Engine();
        engine.start();
    }
}