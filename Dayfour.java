
abstract class Device {
    String name;
    boolean isOn;

    Device(String name) {
        this.name = name;
        this.isOn = false;
    }

    void turnOn() {
        isOn = true;
        System.out.println(name + " is ON");
    }

    void turnOff() {
        isOn = false;
        System.out.println(name + " is OFF");
    }

    abstract void displayStatus();
}

class Light extends Device {
    Light(String name) {
        super(name);
    }

    @Override
    void displayStatus() {
        System.out.println("Light - " + name + " : " + (isOn ? "ON" : "OFF"));
    }
}


class Fan extends Device {
    int speed;

    Fan(String name) {
        super(name);
        speed = 1;
    }

    void setSpeed(int speed) {
        this.speed = speed;
        System.out.println(name + " speed set to " + speed);
    }

    @Override
    void displayStatus() {
        System.out.println("Fan - " + name + " : " + (isOn ? "ON" : "OFF") + ", Speed: " + speed);
    }
}


class AC extends Device {
    int temperature;

    AC(String name) {
        super(name);
        temperature = 24;
    }

    void setTemperature(int temp) {
        temperature = temp;
        System.out.println(name + " temperature set to " + temperature + "C");
    }

    @Override
    void displayStatus() {
        System.out.println("AC - " + name + " : " + (isOn ? "ON" : "OFF") + ", Temp: " + temperature + "C");
    }
}


public class Dayfour{
    public static void main(String[] args) {

        Light light = new Light("Living Room Light");
        Fan fan = new Fan("Ceiling Fan");
        AC ac = new AC("Bedroom AC");

     
        light.turnOn();
        light.displayStatus();
        light.turnOff();

      
        fan.turnOn();
        fan.setSpeed(2);
        fan.displayStatus();
        fan.turnOff();

     
        ac.turnOn();
        ac.setTemperature(20);
        ac.displayStatus();
        ac.turnOff();
    }
}