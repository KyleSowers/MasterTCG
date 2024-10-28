//Lorcana Card JSON Database

var lorcanadb = {
    "firstChapter": [
        {
            "cardImg": "../media/LorcanaCh1/LorcanaCh1_001-204_Ariel-OnHumanLegs.webp",
            "number": "001",
            "setId": "001/204\u2022EN\u20221",
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
            "cardImg": "../media/LorcanaCh1/LorcanaCh1_002-204_Ariel-SpectacularSinger.webp",
            "number": "002",
            "setId": "002/204\u2022EN\u20221",
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
            "cardImg": "../media/LorcanaCh1/LorcanaCh1_003-204_Cinderella-GentleAndKind.webp",
            "number": "003",
            "setId": "003/204\u2022EN\u20221",
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
        },
        {
            "cardImg": "../media/LorcanaCh1/LorcanaCh1_003-204_Cinderella-GentleAndKind.webp",
            "number": "003",
            "setId": "003/204\u2022EN\u20221",
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
        },
        {
            "cardImg": "../media/LorcanaCh1/LorcanaCh1_003-204_Cinderella-GentleAndKind.webp",
            "number": "003",
            "setId": "003/204\u2022EN\u20221",
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
        $.each(firstChapterList, function(i, val) {
            var cardImg = val.cardImg
            var number = val.number;
            var setId = val.setId;
            var name = val.name;
            var subname = val.subname;
            var ink = val.ink;
            var classification = val.classification;
            var rarity = val.rarity;
            var cost = val.cost;
            var strength = val.strength;
            var willpower = val.willpower;
            var loreValue = val.loreValue;
            var artist = val.artist;
            var description = val.description;

            $('.cardContainer1').append('    <div class="cardSingleJSON">' +
                '                <h3 class="cardTitleGold">'+name+'</h3>\n' +
                '                <p class="cardTitleGold">'+subname+'</p>\n' +
                '                <div class="cardCenter">\n' +
                '                    <img src="' + cardImg + '" alt="' + name + ' ' + subname + '">' +
                '                    <div class="cardSelectors">\n' +
                '                        <form action="#" method="POST" class="cardForm">\n' +
                '                            <label for="en_1_base_001/204" style="writing-mode: horizontal-tb;"><span class="cardLabelText1">Base</span>\n' +
                '                                <input type="checkbox" name="card" id="en_1_base_001/204" value="Base-EN-1-1/204" class="cardFormInput">\n' +
                '                            </label>\n' +
                '                            <label for="en_1_foil_001/204" style="writing-mode: horizontal-tb;"><span class="cardLabelText2" style="margin-left: 20px;">Foil</span>\n' +
                '                                <input type="checkbox" name="card" id="en_1_foil_001/204" value="Foil-EN-1-1/204" class="cardFormInput">\n' +
                '                            </label>\n' +
                '                        </form>\n' +
                '                    </div>\n' +
                '                </div>\n' +
                '                <h3 class="cardID">'+setId+'</h3>\n' +
                '           </div>')

        });
}

lorcanaCardData();