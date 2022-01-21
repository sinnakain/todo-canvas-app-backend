require: slotfilling/slotFilling.sc
  module = sys.zb-common

patterns:
    $AnyText = $nonEmptyGarbage
    
    # Паттерны для запуска сценария
    $OpenSkipWords = (хочу|мне|мое|моё|пожалуйста|в|прошу|тебя|может|с)
    $OpenKeyWords = (включи|включить|включай|запусти|запустить|запускай|играть|
        поиграть|поиграем|навык|игра|игру|скил|скилл|приложение|апп|сыграем|
        открой|поиграй со мной|сыграть|давай играть|активируй|давай|поиграем)
    $projectName = (Название вашего навыка)


theme: /
    state: Запуск
        # При запуске приложения с кнопки прилетит сообщение /start.
        q!: $regex</start>
        
        # При запуске приложения с голоса прилетит сказанная фраза.
        q!: [$repeat<$OpenSkipWords>] 
            $repeat<$OpenKeyWords>
            [$repeat<$OpenSkipWords>] 
            $projectName
        script:
            log($jsapi.cailaService.getCurrentClassifierToken());
            $temp.appeal = $request.rawRequest.payload.character.appeal;
            
        if: $temp.appeal == "official"
            a: Добро пожаловать в заметки! Чтобы добавить новую, просто скажите "Запомни" и  нужный текст.
        elseif: $temp.appeal == "no_official"
            a: Добро пожаловать в заметки! Чтобы добавить новую, просто скажи "Запомни" и  нужный текст.
        else:
            a: Добро пожаловать в заметки!

    state: Fallback
        event!: noMatch
        a: Я не понимаю.


    state: ПростойАктион
        event!: simple_action
        event!: SIMPLE_ACTION
        
        script:
            var url = "https://sfzrwp00vg.execute-api.us-east-2.amazonaws.com/default/nodejs-hello-world-function";
        
            var response = $http.query(url);
            if (response.isOk) {
                $temp.rawBody = response.data;
            }
            $temp.rawBody = response.isOk ? $temp.rawBody : "No data!";
            
            var rawResponse = {
                type: 'raw',
                body: {
                    items: [
                        {
                          "command": {
                            "type": "smart_app_data",
                            "smart_app_data": {
                              "result": "" + $temp.rawBody
                            }
                          }
                        }
                    ],
                }
            };
            
            // $context.response = rawResponse;
            
            $response.replies = ($response.replies || []);
            $response.replies.push(rawResponse);
            
        
        a: Hello world {{$temp.rawBody}}!