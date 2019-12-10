const React = require('react');

import './card.css';

class Card extends React.Component {

	constructor(props) {
        super(props);
        this.handleCardTurn = this.handleCardTurn.bind(this); 
    }

    handleCardTurn() {
        this.props.onClick(this.props.index);
    }

	render() {
        const isFaceDown = (this.props.card == 0);
        const cardStyle = isFaceDown ? 'faceDown' : 'faceUp';
        const cardText = isFaceDown ? '' : this.props.card;
        const cardColour = isFaceDown ? '#553000' : '#fff8ce';
        const imgZIndex = isFaceDown ? -10 : 10;
        
		return (
            <span className='container' onClick={this.handleCardTurn}>
                <svg className='backgroundCircle' height="100" width="100">
                    <circle cx="45" cy="45" r="40" stroke="#0d2b0c" strokeWidth="3" fill={cardColour} />
                </svg> 
                <span className={'cardText ' + cardStyle} href='#'>{cardText}</span>
                {
                    !isFaceDown && this.props.showPictureCards ? (
                        <img className='cardImage' src={this.props.image} />
                    ) : ''
                }
                
            </span>
		)
	}
}

export default Card;