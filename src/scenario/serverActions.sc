# Когда приходит ServerAction, сообщение прилетает в состояние, 
# с условием на вход action_id.
# пример:
#     event!: action_id

theme: /
    state: ПростойАктион
        event!: simple_action
        event!: SIMPLE_ACTION
        
        script:
        
            var url = "https://sfzrwp00vg.execute-api.us-east-2.amazonaws.com/default/nodejs-hello-world-function";
        

            var response = $http.query(url);
            if (response.isOk) {
                $temp.rawBody = response.data;
            }
            $jsapi.log("Http response data: " + response.data);
            $jsapi.log(Object.keys($response));
            
            var rawResponse = {
                type: 'raw',
                body: {
                    items: [
                        {
                            bubble: {
                                text: '*Привет всем!*',
                                markdown: true,
                            },
                        },
                    ],
                }
            };
            
            $context.response = { dsds: "[eq"};
            
            // $response.responseData.replies.push(rawResponse);
            
        
        a: Hello world {{$temp.rawBody}}!
    
    
    state: ЗаданиеВыполнено
        event!: done
        event!: DONE

        script:
            $temp.gender = $request.rawRequest.payload.character.gender;
            
        if: $request && $request.data && $request.data.eventData && $request.data.eventData.note
            if: $temp.gender == "male"
                a: Закрыл {{ $request.data.eventData.note }}! Молодец!
            elseif: $temp.gender == "female"
                a: Закрыла {{ $request.data.eventData.note }}! Молодец!
            else:
                a: Закрыто
        else:
            random: 
                a: Молодец!
                a: Красавчик!
                a: Супер!
                
        buttons:
            "Запиши купить молоко"
            "Добавь запись помыть машину"
            "Выйди"

           
    state: ДобавленаНоваяЗапись
        event!: note_added
        event!: NOTE_ADDED

        random: 
            a: Добавлено!
            a: Сохранено!
            a: Записано!
        
        buttons:
            "Запиши купить молоко"
            "Добавь запись помыть машину"
            "Выйди"
            