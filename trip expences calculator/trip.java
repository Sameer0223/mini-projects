import java.util.*;

class Trip {
    public static void main(String[] args) {
        Scanner s=new Scanner(System.in);

        // Ask for the number of people
        System.out.print("Hi! How many people are sharing the expenses? ");
        int numPeople=s.nextInt();

        String[] names=new String[numPeople];
        double[] contributions=new double[numPeople];
        double totalSpent=0.0;

        // Collect each person's name and contribution
        for(int i=0;i<numPeople;i++) {
            System.out.print("Please enter the name of person " + (i+1) + ": ");
            names[i]=s.next();
            System.out.print("How much did " + names[i] + " contribute? ");
            contributions[i]=s.nextDouble();
            totalSpent+=contributions[i];
        }

        // Calculate the average contribution
        double averageContribution=totalSpent/numPeople;
        double[] balances=new double[numPeople];
        for(int i=0;i<numPeople;i++) {
            balances[i]=contributions[i]-averageContribution;
        }

        // Display total spent and average contribution
        System.out.println("\nTotal amount spent by the group: " + totalSpent);
        System.out.println("Each person's average share is: " + averageContribution);
        
        // Show balances (who owes what or should receive what)
        System.out.println("\nHere's what each person owes or should receive:");
        for(int i=0;i<numPeople;i++) {
            System.out.println(names[i] + ": " + String.format("%.2f", balances[i]));
        }

        // Calculate and display settlements
        List<String> settlements=calculateSettlements(names, balances);
        System.out.println("\nHere are the suggested settlements:");
        for(String settlement:settlements) {
            System.out.println(settlement);
        }

        s.close();
    }

    private static List<String> calculateSettlements(String[] names, double[] balances) {
        List<String> settlements=new ArrayList<>();
        int numPeople=names.length;

        // Priority queues for debtors and creditors
        PriorityQueue<Person> debtors=new PriorityQueue<>(Comparator.comparingDouble(p->p.balance));
        PriorityQueue<Person> creditors=new PriorityQueue<>((p1, p2)->Double.compare(p2.balance,p1.balance));

        for(int i=0;i<numPeople;i++) {
            if(balances[i]<0) {
                debtors.offer(new Person(names[i], balances[i]));
            } else if(balances[i]>0) {
                creditors.offer(new Person(names[i], balances[i]));
            }
        }

        // Settle debts between debtors and creditors
        while(!debtors.isEmpty()&&!creditors.isEmpty()) {
            Person debtor=debtors.poll();
            Person creditor=creditors.poll();

            double settlementAmount=Math.min(-debtor.balance, creditor.balance);

            settlements.add(debtor.name + " owes " + creditor.name + " " + String.format("%.2f", settlementAmount));

            debtor.balance+=settlementAmount;
            creditor.balance-=settlementAmount;

            if(debtor.balance<0) {
                debtors.offer(debtor);
            }
            if(creditor.balance>0) {
                creditors.offer(creditor);
            }
        }

        return settlements;
    }

    static class Person {
        String name;
        double balance;

        Person(String name, double balance) {
            this.name=name;
            this.balance=balance;
        }
    }
}
