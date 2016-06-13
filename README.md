SkypeKit Discontinued
======================

This project is obsolete as Microsoft has turned off the SkypeKit. You can read more at [Skype Shuts Down SkypeKit and the Skype Developer Website](http://www.disruptivetelephony.com/2014/07/skype-shuts-down-skypekit-and-the-skype-developer-website.html). This is somewhat old news from Summer of 2014 but as there is still interest in the project now in 2016 I thought I'll post this for clarity!

The project is hosted here just for informational purposes. You won't be able to get it to run now as there isn't a API service the SDK can talk to.

R.I.P. SkypeKit

Skype Bot
=============

1 minute intro
_____________

NB! Compiles and runs against something that I got from skype as: **sdp-distro-desktop-skypekit_4.5.0.105_2859374.tar.gz**

To run it:

    cp project.properties personal.properties
    
fill personal properties with sensible values

    mkdir plugins # this dir is checked for plugins
    mvn clean install 
    cp skype-bot-plugin-example/target/skype-bot-plugin-example-0.0.1-SNAPSHOT.jar plugins/
    
run your copy of skype runtime

    java -jar skype-bot-core/target/skype-bot-0.0.1-SNAPSHOT.jar
    
    
try: "~help" in some skype chat where the bot is present.

Check out *skype-bot-plugin-api* and *skype-bot-plugin-example* if you want to add commands.

Currently it only supports loading plugins from a local directory, there are plans to add dropbox support.

Also only reactive commands for now.


Getting Started
-----------------------

I have written 2 posts about getting started, these will hold your hand on every step. Check them out
and write your own Skype bot today :)

 * [Skype Bot for Fun and Profit - Intro](http://toomasr.com/blog/2013/05/27/skype-bot-for-fun-and-profit/)
 * [Getting Started - everything you need before coding](http://toomasr.com/blog/2013/05/27/skype-bot-for-fun-and-profit-part-I/)
 * [Getting Running - lets get this puppy on the rode](http://toomasr.com/blog/2013/05/27/skype-bot-for-fun-and-profit-part-II/)

Running the Software
--------------------

You will need:

 * skype username/password
 * join skype developer program for 10 USD at http://developer.skype.com
 * create an application at http://developer.skype.com 
 * download a runtime for that application and keep it running (bot connects to that)
 * download your application certificate from http://developer.skype.com (the bot will use it)

Most configuration can be done by copying project.properties to personal.properties
and making changes there.

If you have any questions just drop me a line [@toomasr](http://twitter.com/#!/toomasr) or me [@shelajev](https://twitter.com/shelajev)

Where does this all fit in?
---------------------------

Here is a graph that explains where this Skype Bot fits into the grand scheme. You will also
see that the bot have means to take messages from 3rd party services via POST requests to the
embedded web server.

![Architecture](https://raw.github.com/toomasr/skype-bot/master/shots/skype-bot.png)
