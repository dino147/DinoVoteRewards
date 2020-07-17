# DinoVoteRewards
DinoVoteRewards spigot plugin.
Spigot link: https://www.spigotmc.org/resources/dinovoterewards-1-8-x-1-16-x.80534/

Did you ever wanted to reward your players for voting the server ?
Well, this plugin is for you. You can send commands (like giving some items, money, crates, etc) and broadcast a message when someone votes.

Fully configurable, you can add as many commands as you want, change all the plugin messages, etc.


<b>Dependencies:</b>
<br />
<b>Votifier/NuVotifier</b>
I recomend to install the latest NuVotifier version from here: https://www.spigotmc.org/resources/nuvotifier.13449/
<br />
<b>PlaceHolderAPI<b>
Latest version from spigot: https://www.spigotmc.org/resources/placeholderapi.6245/
Only if you want to use the placeholders to show the number of votes in a scoreboard or just to make a leaderboard using leaderheads.

If you don't install Votifier or NuVotifier, the plugin won't work !

<b><u>Commands:<u><b>

/vote - Display the vote links, the message displayed is configurable (votemessage option).
/votereward - Displays a message with the rewards you give for voting the server (also configurable in config.yml, voterewardmsg option)
/votes - Displays your vote count
/votes <player> displays a target player vote count (online or offline)
/dinovote version - Displays the plugin's version.
/dinovote reload - Reloads the plugin config, you can use this after you change the messages for example.

<b><u>Permissions:<u></b>
dinovote.votereward - permission to use /votereward
dinovote.version - permission to use /dinovote version
dinovote.reload - permission to use /dinovote reload (to reload the config).
dinovote.vip - default permission to receive vip rewards (you can change vip to whatever you want in the config file).
dinovote.mvp - default permission to receive mvp rewards (you can change vip to whatever you want in the config file).
dinovote.noremind - if you have remindmessages enabled in the config.yml, players with this permission will not receive vote reminders

<b><u>PlaceHolders:<u><b>
%dinovoterewards_votes% - displays the number of votes a player has
(You need to have placeholderapi to use placeholders)
