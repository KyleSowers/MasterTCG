//Lorcana Card JSON Database

var lorcanadb = {
    "firstChapter": [
        {
            "number": "001",
            "name": "Ariel",
            "subname": "On Human Legs",
            "ink": "Amber",
            "classification": "storyborn hero princess",
            "rarity": "Uncommon",
            "cost": "4",
            "strength": "3",
            "willpower": "4",
            "loreValue": "2",
            "artist": "Matthew Robert Davies",
            "description": "VOICELESS: This character can't '' to sing songs"
        },
        {
            "number": "002",
            "name": "Ariel",
            "subname": "Spectacular Singer",
            "ink": "Amber",
            "classification": "storyborn hero princess",
            "rarity": "Super Rare",
            "cost": "3",
            "strength": "2",
            "willpower": "3",
            "loreValue": "1",
            "artist": "Alice Pisoni",
            "description": "MUSICAL DEBUT: When you play this character, look at the top 4 cards of your deck. You may reveal a song card and put it into your hand. Put the rest on the bottom of your deck in any order."
        },
        {
            "number": "003",
            "name": "Cinderella",
            "subname": "Gentle and Kind",
            "ink": "Amber",
            "classification": "storyborn hero princess",
            "rarity": "Uncommon",
            "cost": "4",
            "strength": "2",
            "willpower": "5",
            "loreValue": "2",
            "artist": "Javier Salas",
            "description": "A WONDERFUL DREAM: Remove up to 3 damage from chosen Princess character"
        }
    ]
}

function lorcanaCardData() {
    // console.log(lorcanadb.firstChapter)
    var firstChapterList = lorcanadb.firstChapter;
        $.forEach(firstChapterList, function(i, val) {
            var number = val.number;
            var name = val.name;
            var subname = val.subname;
            var ink = val.ink;
            var classification = val.classification;
            var rarity = val.rarity;
            var cost = val.cost;
            var strength = val.strength;
            var willpower = val.willpower;
            var loervalue = val.loreValue;
            var artist = val.artist;
            var description = val.description;



        });
}

lorcanaCardData();