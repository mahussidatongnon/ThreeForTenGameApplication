package com.miageia2.threefortengame.core.controller

import com.miageia2.threefortengame.common.dto.aiplayer.RegisterGameDTO
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class WebsocketController {

//    @MessageMapping("/players/register")  // Mapping pour l'enregistrement d'un joueur
//    @SendTo("/topic/players/register")    // Envoi au topic après le traitement
//    fun registerPlayer(registerGameDTO: RegisterGameDTO): RegisterGameDTO {
//        println("Nouvel utilisateur : $registerGameDTO")
//        // Traiter les données reçues et renvoyer la réponse (par exemple, enregistrer un joueur)
//        return registerGameDTO  // Retourner l'objet qui sera converti en JSON et envoyé au client
//    }
}