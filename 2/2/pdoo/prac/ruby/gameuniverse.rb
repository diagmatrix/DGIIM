#encoding:utf-8

require "./lib/GameUniverseToUI.rb"
require "./lib/GameStateController.rb"
require_relative "./dice.rb"
require_relative "./spacestation.rb"
require_relative "./enemystarship"
require_relative "./gamecharacter.rb"
require_relative "./combatresult.rb"

module Deepspace
  # Clase que desarrolla la partida
  class GameUniverse
    # Puntos necesarios para ganar
    @@WIN = 10
    
    attr_reader :gameState

    def initialize
      @gameState = GameStateController.new
      @turns = 0
      @currentStationIndex = 0
      @dice = Dice.new
      @currentStation = nil
      @currentEnemy = nil
      @spaceStations = Array.new
    end

    # Se realiza un combate entre una estación espacial y un enemigo
    def combatGo(station,enemy)
      ch = @dice.firstShot()
      # Empieza el enemigo
      if ch==GameCharacter::ENEMYSTARSHIP
        fire = enemy.fire
        result = station.recieveShot(fire)
        
        if result==ShotResult::RESIST
          fire = station.fire
          result = enemy.recieveShot(fire)
          enemyWins = (result==ShotResult::RESIST)
        else
          enemyWins = true
        end
      # Empieza la nave
      else
        fire = station.fire
        result = enemy.recieveShot(fire)
        enemyWins = (result==ShotResult::RESIST)
      end

      # Gana el enemigo
      if enemyWins
        s = station.getSpeed
        moves = @dice.spaceStationMoves(s)
        if !moves
          damage = enemy.damage
          station.setPendingDamage(damage)
          return CombatResult::ENEMYWINS
        else
          station.move
          return CombatResult::SPACESTATIONESCAPES
        end
      # No gana el enemigo
      else
        aLoot = enemy.loot
        station.setLoot(aLoot)
        return CombatResult::SPACESTATIONWINS
      end
    end

    # Realiza el combate de un turno
    def combat
      state = @gameState.state
      if state==GameState::BEFORECOMBAT or state==GameState::INIT
        combatResult = combat(@currentStation,@currentEnemy)
        @gameState.next(@turns,@spaceStations.length)
      else
        combatResult = CombatResult::NOCOMBAT
      end
      return combatResult
    end
    
    # La nave del turno descarta el hangar
    def discardHangar
      if @gameState.state==GameState::INIT or @gameState.state==GameState::AFTERCOMBAT
        @currentStation.discardHangar
      end
    end

    # La nave del turno descarta un potenciador de escudo
    def discardShieldBooster(i)
      if @gameState.state==GameState::INIT or @gameState.state==GameState::AFTERCOMBAT
        @currentStation.discardShieldBooster(i)
      end
    end

    # La nave del turno descarta un potenciador de escudo de su hangar
    def discardShieldBoosterInHangar(i)
      if @gameState.state==GameState::INIT or @gameState.state==GameState::AFTERCOMBAT
        @currentStation.discardShieldBoosterInHangar(i)
      end
    end

    # La nave del turno descarta un arma
    def discardWeapon(i)
      if @gameState.state==GameState::INIT or @gameState.state==GameState::AFTERCOMBAT
        @currentStation.discardWeapon(i)
      end
    end

    # La nave del turno descarta un arma de su hangar
    def discardWeaponInHangar(i)
      if @gameState.state==GameState::INIT or @gameState.state==GameState::AFTERCOMBAT
        @currentStation.discardWeaponInHangar(i)
      end
    end

    def getUIVersion
      GameUniverseToUI.new(self)
    end

    # Comprueba si la nave del turno ha ganado la partida
    def haveAWinner
      if @currentStation.nMedals>=@@WIN
        return true
      end
      return false
    end

    # Comienza la partida
    def init(names)
      state = @gameState.state
      if state==GameState::CANNOTPLAY
        dealer = CardDealer.instance
        names.each do |name|
          supplies = dealer.nextSuppliesPackage
          station = SpaceStation.new(name,supplies)
          @spaceStations << station
          
          nh = @dice.initWithNHangars
          nw = @dice.initWithNWeapons
          ns = @dice.initWithNShields
          lo = Loot.new(0,nw,ns,nh,0)
          station.setLoot(lo)
        end

        @currentStationIndex = @dice.whoStarts(@spaceStations.length)
        @currentStation = @spaceStations[@currentStationIndex]
        @currentEnemy = dealer.nextEnemy
        @gameState.next(@turns,@spaceStations.length)
      end
    end

    # Monta un escudo con índice 'i' en la nave
    def mountShieldBooster(i)
      if @gameState.state==GameState::INIT or @gameState.state==GameState::AFTERCOMBAT
        @currentStation.mountShieldBooster(i)
      end
    end

    # Monta un arma con índice 'i' en la nave
    def mountWeapon(i)
      if @gameState.state==GameState::INIT or @gameState.state==GameState::AFTERCOMBAT
        @currentStation.mountWeapon(i)
      end
    end

    # Pasa el turno si se puede
    def nextTurn
      state = @gameState.state
      if state==GameState::AFTERCOMBAT
        stationState = @currentStation.validState
        if stationState
          @currentStationIndex = (@currentStationIndex+1) % @spaceStations.length
          @turns += 1
          @currentStation = @spaceStations[@currentStationIndex]
          @currentStation.cleanUpMountedItems
          dealer = CardDealer.instance
          @currentEnemy = dealer.nextEnemy
          @gameState.next(@turns,@spaceStations.length)
          return true
        end
        return false
      end
      return false
    end

    def to_s
      getUIVersion.to_s
    end
  end
end