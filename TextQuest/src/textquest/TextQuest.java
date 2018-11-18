package textquest;

import java.util.ArrayList;
import java.util.Scanner;

public class TextQuest 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        //System.setProperty("console.encoding","Cp866");//для ввода русских символов
        Scanner in = new Scanner(System.in);
        //in.nextLine()//получение данных типа стринг введенных пользователем с клавиатуры
        //int num = in.nextInt();//получение данных типа инт введенных пользователем с клавиатуры
        boolean keyGameRun = true; //ключИграБежит = правда;
        
        ArrayList<Location> magicLocations = new ArrayList<>();
        ArrayList<Thing> magicThings = new ArrayList<>();
        
        Location garden = new Location(0,0,"garden");
        Location livingRoom = new Location(1,0,"livingRoom");
        Location attic = new Location(1,1,"attic");
        
        Character man = new Character(0,0,"man");
        
        Thing bucket = new Thing(0,0,"bucket",true,true);
        Thing chain = new Thing(0,0,"chain",true,false);
        Thing well = new Thing(0,0,"well",false,false);
        Thing burner = new Thing(1,1,"burner",false,false);
        Thing mag = new Thing(1,0,"mag",false,false);
        Thing alcohol = new Thing(0,0,"alcohol",true,true);
        Thing frog = new Thing(0,0,"frog",false,false);
        
        
        bucket.applicableThings.add("chain");
        bucket.applicableThings.add("burner");
        bucket.applicableThings.add("well");
        bucket.applicableThings.add("mag");
        
        
        ////bucket.applicableThings.add("chain");
        ////bucket.applicableThings.add("burner");
        
        ////well.applicableThings.add("bucket_chain_burner");
        //well.applicableThings.add("chain");
        //well.applicableThings.add("burner");
        
        //mag.applicableThings.add("well");
        ////mag.applicableThings.add("well_bucket_chain_burner");
        //mag.applicableThings.add("chain");
        //mag.applicableThings.add("burner");
        
        
        
        
        magicLocations.add(garden);
        magicLocations.add(livingRoom);
        magicLocations.add(attic);
        
        magicThings.add(bucket);
        magicThings.add(chain);
        magicThings.add(well);
        magicThings.add(burner);
        magicThings.add(mag);
        magicThings.add(alcohol);
        magicThings.add(frog);
        
        Interactive gamePlay = new Interactive();
        
        System.out.println("Welcome to the game!");
        System.out.println("possible commands: inventar, look around,");
        System.out.println("go west, go east, go up, go down, exit,");
        System.out.println("catch THING, apply THING1 THING2");
        
        while(keyGameRun)
        {
            //String textOfUser = in.nextLine();
            //System.out.println(textOfUser);
            gamePlay.recognizeUserCommand(in.nextLine(),magicThings,man,magicLocations);
        }
    }
}

class MaterialObjectInTheWorld
{
    public int x = 0;
    public int y = 0;
    public String name = "";
}


class Location extends MaterialObjectInTheWorld
{
    Location(int thisX, int thisY, String thisName)
    {
        x = thisX;
        y = thisY;
        name = thisName;
    }
}

class Character extends MaterialObjectInTheWorld
{
    Character(int thisX, int thisY, String thisName)
    {
        x = thisX;
        y = thisY;
        name = thisName;
    }
    
    void go(int dx, int dy)
    {
        x += dx;
        y += dy;
    }
}

class Thing extends MaterialObjectInTheWorld
{
    boolean taken = false;  //предмет взятый = неправда
    boolean canTake = true; //предмет можно взять = правда //т.е. подбираемость предмета
    
    ArrayList<String> applicableThings = new ArrayList<>();
    int appliedCounter = 0;
    //ArrayList<String> appliedThings = new ArrayList<>();
    
    Thing(int thisX, int thisY, String thisName, boolean thisCanTake, boolean thisTaken)
    {
        x = thisX;
        y = thisY;
        name = thisName;
        canTake = thisCanTake;
        taken = thisTaken;
    }
}

class Interactive
{
    void recognizeUserCommand(String userCommand, ArrayList<Thing> things, Character magStudent, ArrayList<Location> locations)
    {
        //если введена команда "инвентарь"
        if(userCommand.equals("inventar"))
        {
            //перебрать все вещи и вывести подобранные
            for(Thing th: things)
            {
                if(th.taken == true)
                {
                    System.out.print(" "+th.name+",");
                }
            }
            System.out.println(".");
        }
        //если введена команда "осмотреться"
        else if(userCommand.equals("look around"))
        {
            lookAround(magStudent, locations);
            
            //перебрать все вещи и вывести неподобранные и находящиеся в той же локации что и персонаж
            System.out.print("There is:");
            for(Thing th: things)
            {
                if(th.taken == false && th.x == magStudent.x && th.y == magStudent.y)
                {
                    System.out.print(" "+th.name+",");
                }
            }
            System.out.println(".");
        }
        //если введена команда "идти запад"
        else if(userCommand.equals("go west"))
        {
            moveCharacter(1,0, magStudent, locations, things);
        }
        //если введена команда "идти восток"
        else if(userCommand.equals("go east"))
        {
            moveCharacter(-1,0, magStudent, locations, things);
        }
        //если введена команда "идти вверх"
        else if(userCommand.equals("go up"))
        {
            moveCharacter(0,1, magStudent, locations, things);
        }
        //если введена команда "идти вниз"
        else if(userCommand.equals("go down"))
        {
            moveCharacter(0,-1, magStudent, locations, things);
        }
        //если введена команда "подобрать"
        else if(userCommand.matches("catch.*"))
        {
            //System.out.println("you enter catch");
            for(Thing th: things)
            {
                //if(th.taken == true)
                if(userCommand.matches("catch "+th.name))
                {
                    if(th.taken == true)
                    {
                        System.out.println("It's already catched");
                    }
                    else if(th.canTake == false)
                    {
                        System.out.println("It can't be catched");
                    }
                    else
                    {
                        th.taken = true;
                        System.out.println("You catch: "+th.name);
                    }
                    //System.out.print(" "+th.name+",");
                    
                }
            }
        }
        //если введена команда "применить"
        else if(userCommand.matches("apply.*"))
        {
            String[] splited = userCommand.split("\\s+");
            /*
            if(splited.length == 3)//соединяются два предмета (первое слово в массиве - это команда "применить")
            {
                for(Thing th: things)
                {
                    if(th.name.equals(splited[1]))//найден предмет который применяется к чему либо
                    {
                        if(th.applicableThings.size()==1)//проверка на невыход за пределы списка
                        {
                            if( th.applicableThings.get(0).equals(splited[2]) )//если предметы соединяемые
                            {
                                th.appliedThings.add(splited[2]);//присоединить к первому предмету второй предмет
                                th.name += "_"+splited[2];//переименовать предмет в предмет с присоединенными к нему предметами
                                System.out.println("You applied "+splited[1]+" to "+splited[2]+"!");
                                //проверить закончена ли успешно игра
                                if(th.name.equals("bucket_chain_burner_well_mag"))
                                {
                                    System.out.println("You win!!!");
                                    System.exit(0);
                                }
                            }
                            else
                            {
                                System.out.println("These things are not applicable.");
                            }
                        }
                        else
                        {
                            System.out.println("These things are not applicable. ");
                        }
                    }
                }
            }
            */
            
            
            /*
            else if(splited.length == 4)//соединяются три предмета (первое слово в массиве - это команда "применить")
            {
                for(Thing th: things)
                {
                    if(th.name.equals(splited[1]))//найден предмет который применяется к чему либо
                    {
                        if(th.applicableThings.size()==2)//проверка на невыход за пределы списка
                        {
                            if( th.applicableThings.get(0).equals(splited[2]) && th.applicableThings.get(1).equals(splited[3]))
                            {
                                th.appliedThings.add(splited[2]);//присоединить к первому предмету второй предмет
                                th.appliedThings.add(splited[3]);//присоединить к первому предмету третий предмет
                                th.name += "_"+splited[2]+"_"+splited[3];//переименовать предмет в предмет с присоединенными к нему предметами
                                System.out.println("You applied "+splited[1]+" to "+splited[2]+" to "+splited[3]+"!");
                            }
                            else
                            {
                                System.out.println("These things are not applicable.");
                            }
                        }
                        else
                        {
                            System.out.println("These things are not applicable.");
                        }
                    }
                }
            }
            */
            
            
            
            
            if(splited.length == 3)//соединяются два предмета (первое слово в массиве - это команда "применить")
            {
                for(Thing th: things)
                {
                    if(th.name.equals(splited[1]))//найден предмет который применяется к чему либо
                    {
                        
                        if(th.appliedCounter<th.applicableThings.size())//проверка на невыход за пределы списка
                        {
                            if( th.applicableThings.get(th.appliedCounter).equals(splited[2]) )//найдео то к чему применяется предмет
                            {
                                
                                for(Thing th2: things)//найти второй предмет по имени
                                {
                                    if(th2.name.equals(splited[2]))//второй предмет найден
                                    {
                                        if(th.x == magStudent.x && th.y == magStudent.y && th2.x == magStudent.x && th2.y == magStudent.y)//проверка на наличие предметов около персонажа
                                        {
                                            System.out.println("You applied "+splited[1]+" to "+splited[2]+"!");
                                            th.appliedCounter++;
                                            if(th.appliedCounter == th.applicableThings.size())//завершена вся цепь применений поздравить с победой
                                            {
                                                System.out.println("You win!!!");
                                                System.exit(0);
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("You don't have this thing.");
                                        }
                                    }
                                }
                            }
                            else
                            {
                                System.out.println("These things are not applicable.");
                            }
                        }
                        else
                        {
                            System.out.println("These things are not applicable.");
                        }
                        
                        
                        //appliedCounter
                        //for(int i=0; i<th.applicableThings.size(); i++)
                        //{
                            
                        //}
                        /*
                        if(th.applicableThings.size()==1)//проверка на невыход за пределы списка
                        {
                            if( th.applicableThings.get(0).equals(splited[2]) )//если предметы соединяемые
                            {
                                //th.appliedThings.add(splited[2]);//присоединить к первому предмету второй предмет
                                th.name += "_"+splited[2];//переименовать предмет в предмет с присоединенными к нему предметами
                                System.out.println("You applied "+splited[1]+" to "+splited[2]+"!");
                                //проверить закончена ли успешно игра
                                if(th.name.equals("bucket_chain_burner_well_mag"))
                                {
                                    System.out.println("You win!!!");
                                    System.exit(0);
                                }
                            }
                            else
                            {
                                System.out.println("These things are not applicable.");
                            }
                        }
                        else
                        {
                            System.out.println("These things are not applicable. ");
                        }                        
                        */
                    }
                }
            }
            
            else
            {
                System.out.println("Unknown command");
            }
            
        }
        //если введена команда "выход"
        else if(userCommand.equals("exit"))
        {
            System.exit(0);
        }
        else
        {
            System.out.println("incorrect command");
        }
    }
    
    //вспомогательный метод для перемещения персонажа
    private void moveCharacter(int moveX, int moveY, Character mS, ArrayList<Location> lo, ArrayList<Thing> thi)
    {
        boolean locationExists;
        locationExists = false;
        //пребрать все локации и если в нужном направлении их нет то вывести предупреждение
            for(Location loc: lo)
            {
                if(loc.x == mS.x+moveX && loc.y == mS.y+moveY)
                {
                    locationExists = true;
                }
            }
            if(locationExists == true)
            {
                //двинуть персонаж
                mS.go(moveX,moveY);
                //двинуть все подобранные персонажем предметы
                for(Thing th: thi)
                {
                    if(th.taken == true)
                    {
                        th.x = mS.x;
                        th.y = mS.y;
                    }
                }
                //оглядеться
                lookAround(mS, lo);
            }
            else
            {
                System.out.println("incorrect direction");
            }
    }
    
    //вспомогательный метод осмотра вокруг
    private void lookAround(Character mS, ArrayList<Location> lo)
    {
        System.out.print("You are in:");
            //пребрать все локации и вывести ту в которой персонаж
            for(Location loc: lo)
            {
                if(loc.x == mS.x && loc.y == mS.y)
                {
                    System.out.println(" "+loc.name+". ");
                    break;
                }
            }
    }
    
    
}