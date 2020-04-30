/**
 * Count number of elements
 * Get the first element
 * Get the last element
 * Get the first 5 elements
 * Get the last 5 elements
 *
 * hint: use the following methods
 * head
 * last
 * size
 * take
 * tails
 */
val ls = List.range(0,10)
//write you solution here
val size = ls.length
val first = ls.head
val last = ls.last
val firstFive = ls.take(5)
val lastFive = ls.takeRight(5)


/**
 * Double each number from the numList and return a flatten list
 * e.g.res4: List[Int] = List(2, 3, 4)
 *
 * Compare flatMap VS ls.map().flatten
 */
val numList = List(List(1,2), List(3));
//write you solution here
val newList = numList.flatMap(x=>x.map(_*2))
val newList2 = numList.map((x: List[Int]) => x.map(_ * 2)).flatten



/**
 * Sum List.range(1,11) in three ways
 * hint: sum, reduce, foldLeft
 *
 * Compare reduce and foldLeft
 * https://stackoverflow.com/questions/7764197/difference-between-foldleft-and-reduceleft-in-scala
 */
//write you solution here
val list = List.range(1,11).sum
val list2 = List.range(1,11).foldLeft(0)((m:Int,n:Int)=>m+n)
val list3 = List.range(1,11).reduce((x,y)=>x+y)

/**
 * Practice Map and Optional
 *
 * Map question1:
 *
 * Compare get vs getOrElse (Scala Optional)
 * countryMap.get("Amy");
 * countryMap.getOrElse("Frank", "n/a");
 */
val countryMap = Map("Amy" -> "Canada", "Sam" -> "US", "Bob" -> "Canada")
val f1 = countryMap.get("Amy") // will return Canada
val f2 = countryMap.get("edward") // will return None
val f3 = countryMap.getOrElse("edward", "n/a") // will return n/a as it is default value


/**
 * Map question2:
 *
 * create a list of (name, country) tuples using `countryMap` and `names`
 * e.g. res2: List[(String, String)] = List((Amy,Canada), (Sam,US), (Eric,n/a), (Amy,Canada))
 */
val names = List("Amy", "Sam", "Eric", "Amy")
//write you solution here
val pairsList = names.map(x=> (x,countryMap.getOrElse(x,"n/a")))

/**
 * Map question3:
 *
 * count number of people by country. Use `n/a` if the name is not in the countryMap  using `countryMap` and `names`
 * e.g. res0: scala.collection.immutable.Map[String,Int] = Map(Canada -> 2, n/a -> 1, US -> 1)
 * hint: map(get_value_from_map) ; groupBy country; map to (country,count)
 */
//write you solution here
val countryCount = pairsList.map({case (x,y) => y}).groupBy(x=>x).map({case (x,y)=>(x,y.length)})




/**
 * number each name in the list from 1 to n
 * e.g. res3: List[(Int, String)] = List((1,Amy), (2,Bob), (3,Chris))
 */
val names2 = List("Amy", "Bob", "Chris", "Dann")
//write you solution here
var namesWithCount = names2.map(x=>(names2.indexOf(x),x))

/**
 * SQL questions1:
 *
 * read file lines into a list
 * lines: List[String] = List(id,name,city, 1,amy,toronto, 2,bob,calgary, 3,chris,toronto, 4,dann,montreal)
 */
//write you solution here
import scala.io.Source
val filename = "/home/centos/dev/jarvis_data_eng_artem/spark/src/main/resources/employees.csv"
val lines = Source.fromFile(filename).getLines.toList

/**
 * SQL questions2:
 *
 * Convert lines to a list of employees
 * e.g. employees: List[Employee] = List(Employee(1,amy,toronto), Employee(2,bob,calgary), Employee(3,chris,toronto), Employee(4,dann,montreal))
 */
//write you solution here
case class Employee(id:Int,name:String,city:String,age:Int)
val employees = lines.drop(1).map(x=>{
  var values = x.split(",")
  Employee(values(0).toInt,values(1),values(2),values(3).toInt)
})

/**
 * SQL questions3:
 *
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city)
 * FROM employees
 *
 * result:
 * upperCity: List[Employee] = List(Employee(1,amy,TORONTO,20), Employee(2,bob,CALGARY,19), Employee(3,chris,TORONTO,20), Employee(4,dann,MONTREAL,21), Employee(5,eric,TORONTO,22))
 */
//write you solution here
val cityUpperCase = employees.map(x=> Employee(x.id,x.name,x.city.toUpperCase(),x.age))


/**
 * SQL questions4:
 *
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city)
 * FROM employees
 * WHERE city = 'toronto'
 *
 * result:
 * res5: List[Employee] = List(Employee(1,amy,TORONTO,20), Employee(3,chris,TORONTO,20), Employee(5,eric,TORONTO,22))
 */
//write you solution here
val torontoCityUpperCase = employees.map(x=> Employee(x.id,x.name,x.city.toUpperCase(),x.age)).filter(y=>y.city=="TORONTO")

/**
 * SQL questions5:
 *
 * Implement the following SQL logic using functional programming
 *
 * SELECT uppercase(city), count(*)
 * FROM employees
 * GROUP BY city
 *
 * result:
 * cityNum: scala.collection.immutable.Map[String,Int] = Map(CALGARY -> 1, TORONTO -> 3, MONTREAL -> 1)
 */
//write you solution here
val cityCount = employees.map(x=> Employee(x.id,x.name,x.city.toUpperCase(),x.age)).map(x=>x.city).groupBy(x=>x).map({case (m,n)=>(m,n.length)})

/**
 * SQL questions6:
 *
 * Implement the following SQL logic using functional programming
 *
 * SELECT uppercase(city), count(*)
 * FROM employees
 * GROUP BY city,age
 *
 * result:
 * res6: scala.collection.immutable.Map[(String, Int),Int] = Map((MONTREAL,21) -> 1, (CALGARY,19) -> 1, (TORONTO,20) -> 2, (TORONTO,22) -> 1)
 */
//write you solution here
val cityCountAndAge = employees.map(x=> Employee(x.id,x.name,x.city.toUpperCase(),x.age)).map(x=>(x.city,x.age)).groupBy(x=>x).map({case (m,n)=>(m,n.length)})
