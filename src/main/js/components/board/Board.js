import React, { Component } from 'react';

import Card from '../card/Card.js';
import { getGame, postMove, completeTurn } from '../../rest/rest.js';

import stompClient from '../../websocket.js';
import imageLoader from '../../img/images.js';

import './board.css';

class Board extends Component {

	constructor(props) {
		super(props);
        this.state = {game: {board: []}, loaded: false, gameOver: false, showPictureCards: false};
        this.cardClicked = this.cardClicked.bind(this);
        this.turnCard = this.turnCard.bind(this);
        this.cardClicked = this.cardClicked.bind(this);
		this.newMove = this.newMove.bind(this);
		this.toggleCardTheme = this.toggleCardTheme.bind(this);
		this.quit = this.quit.bind(this);
	}

	componentDidMount() {        
		this.loadGame();
		stompClient.register([
			{route: '/topic/newMove', callback: this.newMove},
		]);
	}

	componentDidUpdate() {
		if (!this.state.gameOver && this.state.game.gameOver) {			
			this.props.onGameOver();
			this.setState({gate: false, gameOver: true});
		}
	}

	newMove(response) {
		const gameId = parseInt(response.body, 10);
		if (gameId === this.state.game.id) {
			this.loadGame();
		}
	}

	loadGame() {
		getGame(response => {
			const game = response.entity;
			this.setState({ game: game, loaded: true });
		}, this.props.gameId);
	}

	cardClicked(index) {
        this.turnCard(index);
    }

    turnCard(cardIndex) {
        const { playerId, gameId } = this.props;
        postMove( (response)  => {
            if (response.entity.error) {
                this.props.alert(response.entity.error);
			}
			else {
				const game = response.entity;
				const delay = game.successfulTurn ? 0 : 2500;
				if (game.lock) {
					setTimeout(() => {
						completeTurn(() => {}, this.props.gameId);
					}, delay);
				}
			} 
        }, gameId, playerId, cardIndex);
	}

	toggleCardTheme() {
		this.setState({ showPictureCards: !this.state.showPictureCards });
	}

	quit() {
		this.props.onQuit();
	}

	render() {

        const { game, showPictureCards } = this.state;
		const columns = Math.floor(Math.sqrt(game.cardPairCount * 2) +0.5);	
		const currentPlayer = game.currentPlayer ? game.currentPlayer.name + ' to play...' : '';
		const gameOver = game.gameOver ? (game.winner === null ? 'A draw' : 'Game Over. ' + game.winner.name + ' wins!') : '';
		const moveInformation = game.gameOver ? gameOver : currentPlayer;
		const bottomButtonText = game.gameOver ? 'Leave' : 'Quit';
		const cardThemeButtonText = showPictureCards ? 'numbered cards' : ' picture cards';
		
		return (
			<div id='innerBoardContainer'>
				{
					this.state.loaded ? (
						<div id='score'>
							<div>Score : {game.player1.name} ({game.score1} - {game.score2}) {game.player2.name}</div>
						</div>

					) : ''
				}
				<div id='moveInformation'>{moveInformation}</div>
				<div id='outerCardsContainer'>
					<div id='cardsContainer'>
						{
							game.board.map((card, i) => {
								return (
									<div className='cardDiv' key={i} style={ (i % columns === 0) ? {clear: 'left'} : {}}>
										<Card 
											index={i} 
											card={card} 
											onClick={this.cardClicked} 
											image={imageLoader(card)}
											showPictureCards={showPictureCards}
										/>
									</div>
								)
							})
						}
					</div>
				</div>
				<div id='checkBoxContainer' style={{float: 'left', clear: 'left'}}>
					<span id='cardThemeButton' onClick={this.toggleCardTheme}>{'Show ' + cardThemeButtonText}</span>
				</div>
				<div id='quitButtonContainer' style={{float: 'left', clear: 'left'}}>
					<span id='quitButton' onClick={this.quit}>{bottomButtonText}</span>
				</div>
			</div>
		)
	}
}

export default Board;
