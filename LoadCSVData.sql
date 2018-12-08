USE `Tweets_Database`;

-- INSERT INTO tweet values(682755747516567552,"Smh.",1,0,"2016-01-01 00:00:00","IanKullgren");
SELECT *  from mentioned t LIMIT 10000;

SELECT h.hastagname from tagged h 
WHERE h.tid = (SELECT t.tid from tweet t RIGHT JOIN `user` u ON t.screen_name = u.screen_name 
WHERE u.ofstate = (SELECT u2.ofstate, count(*) as c from `user` u2 group by ofstate order by count(*) desc));

SELECT * from tweet t RIGHT JOIN `user` u ON t.posted_user = u.screen_name
WHERE (SELECT COUNT(ofstate) ;
-- RIGHT JOIN tagged h ON t.tid = h.tid
-- WHERE u.ofstate = (SELECT ofstate as c from `user` u2 group by ofstate order by count(*) desc LIMIT 1);

SELECT u.screen_name, u.category from tweets t RIGHT JOIN `user` ON t.screen_name = u.screen_name RIGHT JOIN `user` 