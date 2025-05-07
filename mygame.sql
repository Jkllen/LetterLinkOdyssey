CREATE DATABASE  IF NOT EXISTS `mygame` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mygame`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: mygame
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `backgrounds`
--

DROP TABLE IF EXISTS `backgrounds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backgrounds` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `image_path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backgrounds`
--

LOCK TABLES `backgrounds` WRITE;
/*!40000 ALTER TABLE `backgrounds` DISABLE KEYS */;
INSERT INTO `backgrounds` VALUES (1,'MainMenu','/assets/backgrounds/mainmenu.png'),(2,'namefield','src/assets/backgrounds/name.png'),(3,'crosstemplate','src/assets/backgrounds/crosstemplate.png'),(4,'settingsmenu','src/assets/backgrounds/options.png'),(5,'settingsmenu','src/assets/backgrounds/options.png');
/*!40000 ALTER TABLE `backgrounds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `choices`
--

DROP TABLE IF EXISTS `choices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `choices` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dialogue_id` int DEFAULT NULL,
  `choice_text` varchar(255) DEFAULT NULL,
  `next_dialogue_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dialogue_id` (`dialogue_id`),
  CONSTRAINT `choices_ibfk_1` FOREIGN KEY (`dialogue_id`) REFERENCES `dialogues` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `choices`
--

LOCK TABLES `choices` WRITE;
/*!40000 ALTER TABLE `choices` DISABLE KEYS */;
INSERT INTO `choices` VALUES (1,11,'I\'m excited to start over.',12),(2,11,'I miss our old place.',13),(3,11,'I\'m nervous but I\'ll try.',14),(6,36,'Sit next to Dawn',37),(7,36,'Sit next to Michael',39),(8,55,'Hang out with Dawn',68),(9,55,'Hang out with Michael',56),(10,70,'Classic Cheeseburger',71),(11,70,'Spicy Jalapeño Beast',72);
/*!40000 ALTER TABLE `choices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dialogues`
--

DROP TABLE IF EXISTS `dialogues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dialogues` (
  `id` int NOT NULL AUTO_INCREMENT,
  `speaker` varchar(255) DEFAULT NULL,
  `text` text,
  `character_image` varchar(255) DEFAULT NULL,
  `background_image` varchar(255) DEFAULT NULL,
  `sfx` varchar(255) DEFAULT NULL,
  `type` varchar(20) DEFAULT 'normal',
  `next_dialogue_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dialogues`
--

LOCK TABLES `dialogues` WRITE;
/*!40000 ALTER TABLE `dialogues` DISABLE KEYS */;
INSERT INTO `dialogues` VALUES (1,'ALARM','Beep beep beep!','alarm.png','bedroom1.png','morningAlarm.mp3','normal',2),(2,'{player}','(YAWNN~)','protagonist.png','bedroom1.png','yawn.mp3','normal',3),(3,'{player}','Ugh... first morning in the new house. New room. New school. New everything.','protagonist.png','bedroom1.png','','normal',4),(4,'{player}','I still can’t believe we moved halfway across the country.','protagonist.png','bedroom1.png','','normal',5),(5,'{player}','Maybe this is my chance to start fresh... or maybe it’ll just be weird.','protagonist.png','bedroom1.png','','normal',6),(6,'Mom','{player}, Breakfast’s ready! Don’t be late on your first day!','eyesClosedHappyMom.png','bedroom1.png','mom6.wav','normal',7),(7,'{player}','Oh crap I gotta get ready, it’ll be embarrassing to be late on the first day.','protagonist.png','bedroom1.png','','normal',8),(8,'Mom','Morning, sweetheart. Sleep okay?','talkingHappyMom.png','diningtable.png','good_morning.wav','normal',9),(9,'{player}','Yeah... I guess. Just getting used to the new room.','protagonist.png','diningtable.png','','normal',10),(10,'Mom','I know it’s a big change. This place feels so quiet without all the familiar noises, right?','talkingMom.png','diningtable.png','','normal',11),(11,'Mom','How are you feeling about everything?','talkingMom.png','diningtable.png','how_are_you_feeling_today.wav','choice',NULL),(12,'Mom','That’s the spirit. A fresh start can be a good thing. You never know what great things are waiting around the corner.','talkingHappyMom.png','diningtable.png','laugh.wav','normal',15),(13,'Mom','I know, honey. I miss it too. It’s okay to feel that way. But give this place a chance, alright? You might end up liking it here more than you think.','talkingMom.png','diningtable.png','sigh2.wav','normal',15),(14,'Mom','Oh honey, just be yourself. People will see how amazing you are.','talkingHappyMom.png','diningtable.png','wah.wav','normal',15),(15,'Mom','Anyways, you better start preparing soon.','talkingHappyMom.png','diningtable.png','ah.wav','normal',NULL),(16,'Mom','Don’t forget your bag and your schedule. First days are always a little chaotic.','talkingHappyMom.png','diningtable.png','','normal',NULL),(17,'{player}','Thanks Mom! I Got it!','smileMom.png','diningtable.png','','normal',NULL),(18,'{player}','So this is it... my new school.','protagonist.png','infrontschool.png',NULL,'normal',19),(19,'{player}','Doesn\'t look too intimidating.. right?','protagonist.png','infrontschool.png','windtreesblow.mp3','normal',20),(20,'{player}','Okay, deep breath. Just walk in like you belong here.','protagonist.png','infrontschool.png',NULL,'normal',21),(21,'???','Hey! You look kinda lost. Are you new here?','dawntalking.png','infrontschool.png','hey.wav','normal',22),(22,'{player}','Yeah, just moved here. I\'m {player}','protagonist.png','infrontschool.png',NULL,'normal',23),(23,'Dawn','Nice to meet you!! I\'m Dawn!','dawnhappytalking.png','infrontschool.png',NULL,'normal',24),(24,'Dawn','You must be really nervous, but don’t you worry. This gal knows the ins and outs of this school like the back of her hand. I practically live here.','dawnhappytalking.png','infrontschool.png',NULL,'normal',25),(25,'BELL RINGS','ting ting ting!','none.png','infrontschool.png','bellAlarm.mp3','normal',26),(26,'Dawn','That\'s the bell. C\'mon, I\'ll walk with you, don\'t wanna get lost on your first day, right?','dawnhappytalking.png','infrontschool.png',NULL,'normal',27),(27,'{player}','Yeah, thanks!','protagonist.png','infrontschool.png',NULL,'normal',28),(28,'Dawn','So here\'s your classroom… Wait a minute… This is my classroom too.. Looks like I\'ll be your official guide after all','dawntalking.png','classnoprof.png',NULL,'normal',29),(29,'???','Looks like someone made a new friend','michaelsmile2.png','classnoprof.png','curious_3.wav','normal',30),(30,'Dawn','*gasp* Michael, you\'re my classmate too!!','dawnwow.png','classnoprof.png','gasp.mp3','normal',31),(31,'Michael','Ahem','mahem1.png','classnoprof.png','clears_throat.wav','normal',32),(32,'Dawn','Oh! {player}, this is Michael, my best/friend/chaos manager. And Michael this is {player}, he\'s new here so don\'t scare him too much','dawnhappytalking.png','classnoprof.png','oh.wav','normal',33),(33,'Michael','That\'s one way to make an introduction. Nice to meet you {player}','michaeltalking.png','classnoprof.png',NULL,'normal',34),(34,'{player}','Nice to meet you too, Michael','protagonist.png','classnoprof.png',NULL,'normal',35),(35,'Teacher','Good morning, everyone. Settle down, please. We have a new student here, they just transferred so please treat them nicely.','teachertalking.png','classwithprof.png',NULL,'normal',36),(36,'Teacher','You all know each other from last year, but for {player}, this is day one. {player}, find somewhere to sit.','teacherpointfinger.png','classwithprof.png',NULL,'choice',NULL),(37,'Dawn','Yessss! Seat buddies! I knew we\'d make a good team. I can\'t wait to get to know each other','dawnhappytalking.png','classwithprof.png',NULL,'normal',38),(38,'Michael','You two are gonna be trouble, I can already tell.','mahem3.png','classwithprof.png',NULL,'normal',41),(39,'Michael','Solid choice. I promise I won’t cause you too much trouble.','michaelpoint.png','classwithprof.png',NULL,'normal',40),(40,'Dawn','Betrayed on day one?! I see how it is! Kidding, kidding. It’s good you want to get to know Michael.','dawnsmile.png','classwithprof.png',NULL,'normal',41),(41,'Class Ended','*lunch bell rings* Ding~ Ding~','protagonist.png','classnoprof.png','bellAlarm.mp3','normal',42),(42,'Dawn','Lunch Time! {player}, come sit with us!','dawnhappytalking.png','classnoprof.png',NULL,'normal',43),(43,'{player}','Yeah, sure!','protagonist.png','classnoprof.png',NULL,'normal',44),(44,'Dawn','So what do you think of the school so far? Be honest!','dawntalking.png','canteen1.png','murmursch.mp3','normal',45),(45,'Michael','And don’t let the teacher scare you. She’s like a cat loud, but secretly soft.','michaelsmile.png','canteen1.png',NULL,'normal',46),(46,'{player}','It’s definitely different… but not bad so far.','protagonist.png','canteen1.png',NULL,'normal',47),(47,'Dawn',' Different is good! It means stories are about to happen.','dawnsmile.png','canteen2.png',NULL,'normal',48),(48,'Micheal','Speaking of stories what kind of stuff are you into? Hobbies? Weekend plans?','michaelpoint.png','canteen2.png',NULL,'normal',49),(49,'{player}','Well, I usually......','protagonist.png','canteen2.png',NULL,'normal',50),(50,'Dawn','Well, it was nice chatting and getting to know you','dawnhappytalking.png','canteen2.png',NULL,'normal',51),(51,'Michael','I hope we were able to help you getting used to this new life of yours.','michaelsmile.png','canteen2.png',NULL,'normal',52),(52,'{player}','Yeah thanks, I thought my first day was gonna be weird but… Thanks to you guys, it was much more enjoyable. See you tomorrow guys!','protagonistsmileclosedeyes.png','canteen2.png',NULL,'normal',53),(53,'Phone','*Phone Buzzes*','protagonist.png','bedroom2.png','phoneBuzz.mp3','normal',54),(54,'{player}','Whoa, two invites at once?','protagonist.png','bedroom2phone.png',NULL,'normal',55),(55,'{player}','Michael wants to hang out at his place.. and Dawn\'s asking if I wanna grab burgers.. Guess I have to pick one.','protagonist.png','bedroom2phone.png',NULL,'choice',NULL),(56,'Michael','Hey, glad you made it! Hope the walk wasn\'t too long.','cmht.png','michaelbedroom.png',NULL,'normal',57),(57,'{player}','No problem, your neighborhood\'s kinda peaceful actually.','cmec.png','michaelbedroom.png',NULL,'normal',58),(58,'Michael','Yeah, it\'s not too bad. Quiet helps me focus when I\'m coding.','cmht.png','michaelbedroom.png',NULL,'normal',59),(59,'Michael','I\'ve been messing around with Java lately. Thinking about studying Computer Science in college.','cmht.png','michaelbedroom.png',NULL,'normal',60),(60,'{player}','Oh nice! What got you into programming?','cms.png','michaelbedroom.png',NULL,'normal',61),(61,'Michael','Honestly? Video games. Wanted to make one, got into tutorials, next thing I know I\'m up all night debugging a while loop.','cmpt.png','michaelbedroom.png',NULL,'normal',62),(62,'{player}','Haha, relatable. You must be pretty good at it then?','cms.png','michaelbedroom.png',NULL,'normal',63),(63,'Michael',' I\'m learning. Want to try some code? I can show you something simple in Java. Let\'s build something tiny like printing your name, or a calculator maybe.','cmht.png','michaelbedroom.png',NULL,'normal',64),(64,'{player}','Sure, sounds fun. Let\'s see what you got.','cmec.png','michaelbedroom.png',NULL,'normal',65),(65,'{player}','That was actually pretty fun. I see the appeal.','cms.png','michaelbedroom.png',NULL,'normal',66),(66,'Michael','Told you. Coding\'s just logic puzzles with a keyboard. Anyway, thanks for coming by. It was cool to just chill and talk for a bit.','cmpt.png','michaelbedroom.png',NULL,'normal',67),(67,'{player}','Yeah, same! Let\'s hang out again next time!','cmec.png','michaelbedroom.png',NULL,'normal',82),(68,'Dawn','You came!! Welcome to the best burger spot in town. No exaggeration.','dse.png','burgerjoint.png','restoAmbience.mp3','normal',69),(69,'{player}','Smells amazing already. Thanks for inviting me.','dhs.png','burgerjoint.png',NULL,'normal',70),(70,'Dawn','Of course! Gotta treat the new kid right. So... what\'s your go-to burger order? ','dpc.png','burgerjoint.png',NULL,'choice',NULL),(71,'Dawn',' Haha, nice. You\'ve got good taste','dht.png','burgerjoint.png','laughter.wav','normal',73),(72,'Dawn','You\'re brave!','dht.png','burgerjoint.png',NULL,'normal',73),(73,'Dawn','So, what\'s your vibe? Like, outside of school stuff. Hobbies, secret talents, embarrassing stories? Spill.','dht.png','burgerjoint.png',NULL,'normal',74),(74,'{player}','Hmm.. I\'m into programming most of the time. You?','dht.png','burgerjoint.png',NULL,'normal',75),(75,'Dawn','I\'m a bit of everything. Art, music, you name it. Oh, and I beat Michael at Mario Kart every time. Don\'t let him lie to you.','dpf.png','burgerjoint.png',NULL,'normal',76),(76,'{player}','He seems like a bit competitive.','dpf.png','burgerjoint.png',NULL,'normal',77),(77,'Dawn','Oh, he is. But between you and me he’s a giant nerd. He\'s been learning Java lately. Like… full-on programming mode. He talks about it like it’s magic.','dpf.png','burgerjoint.png','oh.wav','normal',78),(78,'{player}',' He actually invited me over to show me some code.','dpf.png','burgerjoint.png',NULL,'normal',79),(79,'Dawn','Really? That\'s awesome! Told ya he\'s a nerd, but in a good way. We\'re kinda opposites, but that\'s why it works, y\'know?','dse.png','burgerjoint.png',NULL,'normal',80),(80,'{player}','Haha, maybe that\'s why. Anyways, while waiting for our order, wanna play something?','dhs.png','burgerjoint.png',NULL,'normal',81),(81,'Dawn','Sure!','dpc.png','burgerjoint.png',NULL,'normal',82),(82,'Teacher','Finals are next week. I hope you\'ve all been preparing.','teachertalking.png','classwithprof.png',NULL,'normal',83),(83,'Dawn','Ugh. The academic apocalypse is upon us.','der.png','classwithprof.png','annoyed_grumble.wav','normal',84),(84,'Michael','That\'s why we’re studying tomorrow. My place. I\'ve got everything set.','michaeltalking.png','classwithprof.png',NULL,'normal',85),(85,'{player}','Honestly, I really need this. I\'m in!','protagonist.png','classwithprof.png',NULL,'normal',86),(86,'Dawn','Fine. Just promise there\'ll be snacks.','dawnstarry.png','classwithprof.png',NULL,'normal',87),(87,'Michael','Notes, flashcards, a plan. Welcome to Study HQ.','cmpt.png','mstudyplace.png',NULL,'normal',88),(88,'Dawn','This looks like a battlefield of textbooks','dt.png','mstudyplace.png',NULL,'normal',89),(89,'{player}','It\'s kinda impressive though.','protagonist.png','mstudyplace.png',NULL,'normal',90),(90,'Narrator','(In the middle of the study session....) Some time later.....','narrator.png','mstudyplace.png','slowClocktick.mp3','normal',91),(91,'Michael','Alright, this part\'s tricky but think of it like a puzzle. But think about that variable having a coefficient','cmpt.png','mstudyplace.png',NULL,'normal',92),(92,'{player}','Oh! That actually makes sense now!','cmec.png','mstudyplace.png',NULL,'normal',93),(93,'Dawn','Ughh, my brain\'s melting..','da.png','mstudyplace.png','annoyed_grumble.wav','normal',94),(94,'{player}','Focus, Dawn. You\'ve almost got it.','ds.png','mstudyplace.png',NULL,'normal',95),(95,'Michael','*Ahem*, Just one more example, then we take a break.','cmht.png','mstudyplace.png','clears_throat.wav','normal',96),(96,'Narrator','(Later, during break)','narrator.png','mstudyplace.png','slowClocktick.mp3','normal',97),(97,'Dawn','If exams didn\'t exist, I\'d be designing characters all day.','dpc.png','mstudyplace.png',NULL,'normal',98),(98,'Michael','I wanna build something big! Maybe an app or a game.','cmec.png','mstudyplace.png',NULL,'normal',99),(99,'{player}','I just wanna keep improving... and maybe figure things out along the way','cmec.png','mstudyplace.png',NULL,'normal',100),(100,'Dawn','Someday I wanna make something great! Music, comics, anything. Just… create and whatever I want to do, I will do it!','dpc.png','mstudyplace.png',NULL,'normal',101),(101,'Michael','I\'m going all in on Computer Science. Big schools, big dreams.','cmht.png','mstudyplace.png',NULL,'normal',102),(102,'{player}','Not sure where I\'m headed yet, but I want to be ready!','cms.png','mstudyplace.png',NULL,'normal',103),(103,'Narrator','*D-Day = 0* EXAM DAY','narrator.png','classwithprof.png','slowClocktick.mp3','normal',104),(104,'{player}','*Sweat* (Thinking) Final Exam day, I\'m too nervous for this. But I prepared and I studied with the friends I made along the way! Inhale... Exhale.... I can do this!','protagonist.png','classwithprof.png',NULL,'normal',105),(105,'Dawn','I\'m not built for this ugh..','der.png','classwithprof.png',NULL,'normal',106),(106,'Michael','You\'ll be fine. We all studied hard. I\'m sure you can pass.','michaeltalking.png','classwithprof.png',NULL,'normal',107),(107,'Teacher','Here are your exams. Good luck students!','teacherhappytalk.png','classwithprof.png',NULL,'normal',108),(108,'Bell Rings','Beep~ Beep~ Beep~','bell.png','classwithprof.png','examAlarm.mp3','normal',109),(109,'Dawn','It\'s finally over, I can breathe again~','dawnstarry.png','classwithprof.png',NULL,'normal',110),(110,'Michael','I think we nailed it!','michaelsmile2.png','classwithprof.png','sigh_of_relief_1.wav','normal',111),(111,'{player}','I actually.... don\'t feel terrible! That\'s a win!','michaelsmile2.png','classwithprof.png',NULL,'normal',112),(112,'Narrator','*Tuesday - Morning*','narrator.png','classwithprof.png',NULL,'normal',113),(113,'Teacher','I\'ve graded your exams... and there is something I need to say.','teacherpointfinger.png','classwithprof.png',NULL,'normal',114),(114,'Teacher','Good job, everyone!!','teacherhappytalk.png','classwithprof.png',NULL,'normal',115),(115,'Dawn','Hey! I passed! And it\'s not even close!','dawnstarry.png','classwithprof.png',NULL,'normal',116),(116,'Michael','Top Marks! Guess the study session worked.','michaeltalking.png','classwithprof.png','sigh_of_relief_1.wav','normal',117),(117,'{player}','Not perfect, but way better than I hoped. We really pulled through.','michaelsmile2.png','classwithprof.png',NULL,'normal',118),(118,'Narrator','1 year later...','none.png','oneyearlater.png',NULL,'normal',119),(119,'{player}','New school, new friends, and now... a new start. Maybe the future won\'t be so scary after all. Dawn, Michael, I hope to see you guys again in the future.','protagonist.png','oneyearlater.png',NULL,'normal',120),(120,'Developer','This is the end of the game. Thank you for playing our game! This is a game project that we had fun developing despite the imperfections.','protagonist.png','end.png',NULL,'normal',121);
/*!40000 ALTER TABLE `dialogues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `players` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `player_Name` varchar(100) NOT NULL,
  `time_added` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`player_id`),
  UNIQUE KEY `player_id` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
INSERT INTO `players` VALUES (1,'Gusion','2025-04-15 11:59:07'),(2,'asd','2025-04-15 13:07:23'),(3,'s','2025-04-15 13:12:24'),(4,'sad','2025-04-15 13:21:08'),(5,'s','2025-04-15 13:21:53'),(6,'Aust','2025-04-18 05:10:15'),(7,'Ako','2025-04-18 05:11:52'),(8,'asd','2025-04-18 05:21:35'),(9,'Augustine','2025-04-18 05:29:41'),(10,'sdf','2025-04-18 05:35:12'),(11,'asd','2025-04-18 05:39:30'),(12,'sad','2025-04-18 05:49:53'),(13,'d','2025-04-18 05:50:42'),(14,'sd','2025-04-18 05:52:58'),(15,'sd','2025-04-18 05:54:45'),(16,'Augustine','2025-04-18 05:59:56'),(17,'asd','2025-04-18 06:04:19'),(18,'asd','2025-04-18 06:07:44'),(19,'asd','2025-04-18 06:10:26'),(20,'sad','2025-04-18 06:17:10'),(21,'asd','2025-04-18 06:22:36'),(22,'asd','2025-04-18 06:36:03'),(23,'asd','2025-04-18 06:37:04'),(24,'asd','2025-04-18 06:37:30'),(25,'sd','2025-04-18 06:39:30'),(26,'asd','2025-04-18 06:45:04'),(27,'Augustine','2025-04-18 06:46:15'),(28,'Dasaas','2025-04-18 06:47:50'),(29,'Austine','2025-04-18 07:01:03'),(30,'asd','2025-04-18 07:06:26'),(31,'sd','2025-04-19 11:22:51'),(32,'df','2025-04-19 11:34:11'),(33,'sd','2025-04-19 11:36:25'),(34,'sad','2025-04-19 11:36:45'),(35,'sd','2025-04-19 11:37:55'),(36,'sd','2025-04-19 11:41:51'),(37,'asd','2025-04-19 11:45:07'),(38,'asd','2025-04-19 11:45:23'),(39,'asd','2025-04-19 12:40:17'),(40,'asd','2025-04-19 12:43:29'),(41,'ASD','2025-04-19 13:09:49'),(42,'ASD','2025-04-19 13:13:48'),(43,'sad','2025-04-19 13:58:23'),(44,'sd','2025-04-19 14:07:04'),(45,'sda','2025-04-19 14:13:58'),(46,'asd','2025-04-19 14:14:15'),(47,'sd','2025-04-19 14:14:45'),(48,'asd','2025-04-19 14:16:43'),(49,'asd','2025-04-19 14:19:36'),(50,'df','2025-04-19 14:20:19'),(51,'asd','2025-04-19 14:21:37'),(52,'asd','2025-04-19 14:22:25'),(53,'Augustine','2025-04-19 14:29:32'),(54,'sd','2025-04-19 14:36:59'),(55,'sad','2025-04-19 14:46:52'),(56,'asd','2025-04-19 14:48:35'),(57,'sad','2025-04-19 14:51:09'),(58,'asd','2025-04-19 14:53:08'),(59,'asd','2025-04-19 14:56:25'),(60,'asd','2025-04-19 14:58:23'),(61,'ads','2025-04-19 14:59:37'),(62,'asd','2025-04-19 15:00:26'),(63,'asd','2025-04-19 15:02:36'),(64,'sad','2025-04-19 15:03:33'),(65,'dd','2025-04-19 15:05:47'),(66,'ads','2025-04-19 15:07:18'),(67,'asd','2025-04-19 15:08:16'),(68,'sda','2025-04-19 15:09:07'),(69,'asd','2025-04-19 15:09:37'),(70,'asd','2025-04-19 15:11:35'),(71,'df','2025-04-19 15:17:41'),(72,'asd','2025-04-19 15:24:59'),(73,'asd','2025-04-19 15:30:56'),(74,'asd','2025-04-19 15:40:15'),(75,'asd','2025-04-19 15:44:54'),(76,'asd','2025-04-19 15:45:20'),(77,'sad','2025-04-20 10:59:01'),(78,'ad','2025-04-20 11:01:08'),(79,'asd','2025-04-20 11:21:09'),(80,'sad','2025-04-20 11:25:19'),(81,'sad','2025-04-20 13:27:35'),(82,'ads','2025-04-20 13:38:17'),(83,'asd','2025-04-20 13:39:49'),(84,'asd','2025-04-20 13:40:52'),(85,'asd','2025-04-20 13:42:11'),(86,'asd','2025-04-20 13:44:55'),(87,'asd','2025-04-20 13:45:19'),(88,'asd','2025-04-20 13:45:38'),(89,'asd','2025-04-20 13:50:13'),(90,'asd','2025-04-20 13:52:59'),(91,'asd','2025-04-20 13:53:18'),(92,'asd','2025-04-20 13:53:59'),(93,'asd','2025-04-20 13:55:26'),(94,'asd','2025-04-20 13:56:34'),(95,'aasd','2025-04-20 13:57:04'),(96,'asd','2025-04-20 14:06:16'),(97,'ads','2025-04-20 14:13:50'),(98,'asd','2025-04-20 14:21:05'),(99,'asd','2025-04-20 14:27:12'),(100,'Didi','2025-04-20 14:28:25'),(101,'sad','2025-04-20 14:56:51'),(102,'asd','2025-04-20 15:11:51'),(103,'asd','2025-04-20 15:16:05'),(104,'asd','2025-04-20 15:24:00'),(105,'sad','2025-04-20 15:45:56'),(106,'asd','2025-04-20 15:54:50'),(107,'asddd','2025-04-21 13:20:42'),(108,'asd','2025-04-21 13:30:31'),(109,'asd','2025-04-21 13:38:49'),(110,'asd','2025-04-21 13:59:31'),(111,'Auu','2025-04-21 14:11:20'),(112,'asd','2025-04-21 14:12:51'),(113,'sad','2025-04-21 14:16:12'),(114,'Jay','2025-04-21 14:23:41'),(115,'ads','2025-04-21 14:27:01'),(116,'Jayy','2025-04-21 14:29:56'),(117,'Jayy','2025-04-21 14:31:54'),(118,'asd','2025-04-24 14:17:35'),(119,'asd','2025-04-24 14:28:45'),(120,'asd','2025-04-24 14:35:30'),(121,'sad','2025-04-24 14:46:34'),(122,'Save','2025-04-24 14:47:18'),(123,'asd','2025-04-24 14:50:47'),(124,'asd','2025-04-24 14:54:23'),(125,'sad','2025-04-24 14:57:24'),(126,'asd','2025-04-24 15:02:31'),(127,'asd','2025-04-24 15:07:28'),(128,'sad','2025-04-24 15:14:12'),(129,'sad','2025-04-24 15:16:42'),(130,'sad','2025-04-24 15:18:29'),(131,'asd','2025-04-24 15:20:56'),(132,'sad','2025-04-24 15:25:33'),(133,'sdf','2025-04-24 15:26:29'),(134,'asd','2025-04-24 15:32:07'),(135,'sad','2025-04-26 05:54:00'),(136,'Jkllen','2025-04-26 11:28:40'),(137,'sad','2025-04-26 11:32:35'),(138,'sad','2025-04-26 11:49:18'),(139,'asd','2025-04-26 11:50:31'),(140,'dd','2025-04-26 12:01:26'),(141,'sad','2025-04-29 11:30:49'),(142,'sad','2025-04-29 11:31:28'),(143,'asd','2025-04-29 11:37:25'),(144,'asd','2025-04-29 11:47:52'),(145,'ads','2025-04-29 11:48:16'),(146,'asd','2025-04-29 11:54:57'),(147,'asd','2025-04-29 11:59:44'),(148,'sad','2025-04-29 12:01:39'),(149,'asd','2025-04-29 12:01:57'),(150,'asd','2025-04-29 12:04:43'),(151,'sad','2025-04-29 12:05:03'),(152,'sad','2025-04-29 12:29:01'),(153,'asd','2025-04-29 12:29:53'),(154,'sad','2025-04-29 12:32:29'),(155,'asd','2025-04-29 12:34:54'),(156,'Jkllen','2025-05-01 03:16:34'),(157,'as','2025-05-01 07:32:06'),(158,'asd','2025-05-01 07:34:03'),(159,'asd','2025-05-01 07:34:45'),(160,'asd','2025-05-01 08:28:11'),(161,'asd','2025-05-01 08:28:46'),(162,'asd','2025-05-01 08:30:36'),(163,'sad','2025-05-01 08:43:33'),(164,'sad','2025-05-01 08:45:21'),(165,'asd','2025-05-01 08:46:34'),(166,'12','2025-05-01 08:49:12'),(167,'asd','2025-05-01 09:01:07'),(168,'sad','2025-05-01 09:01:51'),(169,'ads','2025-05-01 09:04:30'),(170,'Link','2025-05-01 14:52:57'),(171,'Zelda','2025-05-01 14:59:25'),(172,'Virgil','2025-05-01 15:04:27'),(173,'asd','2025-05-03 05:31:27'),(174,'Jkllen','2025-05-03 05:58:26'),(175,'sad','2025-05-03 06:03:00'),(176,'asd','2025-05-03 06:14:58'),(177,'asd','2025-05-03 06:15:20'),(178,'sad','2025-05-03 06:16:42'),(179,'asd','2025-05-03 06:42:37'),(180,'asd','2025-05-03 06:50:09'),(181,'asd','2025-05-03 06:58:28'),(182,'asd','2025-05-03 08:08:43'),(183,'sad','2025-05-03 08:09:16'),(184,'asd','2025-05-03 08:14:02'),(185,'ds','2025-05-03 08:16:14'),(186,'asd','2025-05-03 09:11:57'),(187,'asd','2025-05-03 09:12:53'),(188,'sad','2025-05-03 09:16:11'),(189,'asd','2025-05-03 09:16:55'),(190,'asd','2025-05-03 09:17:40'),(191,'asd','2025-05-03 09:18:06'),(192,'sd','2025-05-03 09:18:29'),(193,'asd','2025-05-03 09:18:56'),(194,'asd','2025-05-03 09:19:38'),(195,'asd','2025-05-03 09:20:59'),(196,'asd','2025-05-03 09:24:47'),(197,'asd','2025-05-03 09:29:33'),(198,'sad','2025-05-03 09:31:29'),(199,'asd','2025-05-03 09:32:48'),(200,'asd','2025-05-03 09:33:10'),(201,'asd','2025-05-03 09:33:33'),(202,'sad','2025-05-03 09:33:55'),(203,'asd','2025-05-03 09:34:39'),(204,'asd','2025-05-03 09:36:04'),(205,'aasda','2025-05-03 09:38:05'),(206,'asd','2025-05-03 09:41:47'),(207,'asd','2025-05-03 12:51:15'),(208,'asd','2025-05-03 12:53:26'),(209,'asd','2025-05-03 12:53:47'),(210,'asd','2025-05-03 12:54:06'),(211,'asd','2025-05-03 12:54:31'),(212,'asd','2025-05-03 12:55:05'),(213,'asd','2025-05-03 12:57:25'),(214,'asd','2025-05-03 12:58:14'),(215,'asd','2025-05-03 13:01:59'),(216,'asd','2025-05-03 13:29:38'),(217,'sad','2025-05-03 14:22:21'),(218,'asd','2025-05-03 14:31:36'),(219,'asd','2025-05-03 15:35:13'),(220,'asd','2025-05-04 03:56:37'),(221,'asd','2025-05-04 05:03:43'),(222,'hg','2025-05-04 05:17:08'),(223,'Augustine','2025-05-04 06:18:16'),(224,'ad','2025-05-04 06:22:44'),(225,'sad','2025-05-04 06:36:35'),(226,'Vaile','2025-05-04 06:37:01'),(227,'asd','2025-05-04 06:38:02'),(228,'asd','2025-05-04 06:38:12'),(229,'asd','2025-05-04 06:41:05'),(230,'sad','2025-05-04 06:43:19'),(231,'ad','2025-05-04 06:53:26'),(232,'asd','2025-05-04 07:23:14'),(233,'asd','2025-05-04 07:26:16'),(234,'asd','2025-05-04 07:27:31'),(235,'asd','2025-05-04 07:28:11'),(236,'asd','2025-05-04 07:29:31'),(237,'asd','2025-05-04 07:32:07'),(238,'sd','2025-05-04 07:33:33'),(239,'sad','2025-05-04 07:35:22'),(240,'asd','2025-05-04 07:45:33'),(241,'asd','2025-05-04 08:19:38'),(242,'asd','2025-05-04 08:27:03'),(243,'ASD','2025-05-04 09:54:50'),(244,'sd','2025-05-04 10:34:37'),(245,'asd','2025-05-04 10:48:33'),(246,'asd','2025-05-04 11:08:46'),(247,'asd','2025-05-04 11:12:37'),(248,'asd','2025-05-04 11:26:31'),(249,'asd','2025-05-04 13:20:57'),(250,'asd','2025-05-04 14:20:12'),(251,'sad','2025-05-04 14:31:17'),(252,'asd','2025-05-04 14:33:10'),(253,'asd','2025-05-04 14:33:41'),(254,'asd','2025-05-04 14:56:44'),(255,'asd','2025-05-04 15:26:10'),(256,'sad','2025-05-04 15:31:30'),(257,'asd','2025-05-04 15:38:58'),(258,'asd','2025-05-04 15:53:22'),(259,'asd','2025-05-04 16:10:48'),(260,'asd','2025-05-04 16:36:10'),(261,'sad','2025-05-07 15:08:51'),(262,'asd','2025-05-07 15:14:56'),(263,'sad','2025-05-07 15:15:50'),(264,'sad','2025-05-07 15:18:26'),(265,'asd','2025-05-07 15:20:04'),(266,'asd','2025-05-07 16:07:07'),(267,'Vaile','2025-05-07 16:36:14'),(268,'asd','2025-05-07 16:41:15'),(269,'asd','2025-05-07 16:42:36'),(270,'asd','2025-05-07 16:45:37'),(271,'asd','2025-05-07 16:47:39'),(272,'asd','2025-05-07 16:50:06'),(273,'asd','2025-05-07 16:54:47'),(274,'asd','2025-05-07 17:00:54'),(275,'asd','2025-05-07 17:02:46'),(276,'asd','2025-05-07 17:05:32'),(277,'asd','2025-05-07 17:06:26'),(278,'asd','2025-05-07 17:12:00'),(279,'asd','2025-05-07 17:17:45'),(280,'asd','2025-05-07 17:19:22'),(281,'asd','2025-05-07 17:20:11'),(282,'asd','2025-05-07 17:30:25');
/*!40000 ALTER TABLE `players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `saves`
--

DROP TABLE IF EXISTS `saves`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `saves` (
  `slot_id` int NOT NULL,
  `player_name` varchar(255) DEFAULT NULL,
  `dialogue_index` int DEFAULT NULL,
  `screenshot_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`slot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `saves`
--

LOCK TABLES `saves` WRITE;
/*!40000 ALTER TABLE `saves` DISABLE KEYS */;
INSERT INTO `saves` VALUES (1,'asd',0,'screenshots/slot_1.png');
/*!40000 ALTER TABLE `saves` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `id` int NOT NULL,
  `skip_unseen` tinyint(1) DEFAULT NULL,
  `text_speed` double DEFAULT NULL,
  `auto_forward` double DEFAULT NULL,
  `rollback_mode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1,0,1,0.2054794520547945,'Disable');
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-08  1:32:54
