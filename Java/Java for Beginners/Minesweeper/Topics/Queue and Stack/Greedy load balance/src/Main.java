import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        Queue<Task> taskQueue1 = new ArrayDeque<>(num);
        Queue<Task> taskQueue2 = new ArrayDeque<>(num);
        int queueLoad1 = 0;
        int queueLoad2 = 0;
        for (int i = 0; i < num; i++) {
            int taskId = scanner.nextInt();
            int taskLoad = scanner.nextInt();
            if (queueLoad1 > queueLoad2) {
                taskQueue2.offer(new Task(taskId, taskLoad));
                queueLoad2 += taskLoad;
            } else {
                taskQueue1.offer(new Task(taskId, taskLoad));
                queueLoad1 += taskLoad;
            }
        }
        StringBuilder queue1 = new StringBuilder();
        StringBuilder queue2 = new StringBuilder();
        while (!taskQueue1.isEmpty()) {
            queue1.append(taskQueue1.remove().getId());
            queue1.append(" ");
        }
        queue1.deleteCharAt(queue1.length() - 1);
        while (!taskQueue2.isEmpty()) {
            queue2.append(taskQueue2.remove().getId());
            queue2.append(" ");
        }
        queue2.deleteCharAt(queue2.length() - 1);
        System.out.println(queue1);
        System.out.println(queue2);
    }
}

class Task {
    int id;
    int load;

    public Task(int id, int load) {
        this.id = id;
        this.load = load;
    }

    public int getId() {
        return this.id;
    }

    public int getLoad() {
        return this.load;
    }

    public void setLoad(int load) {
        this.load = load;
    }
}