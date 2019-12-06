const React = require('react');

class Seek extends React.Component {

	constructor(props) {
        super(props);
        this.handleSeekAccept = this.handleSeekAccept.bind(this); 
    }

    handleSeekAccept() {
        this.props.onSeekAccepted(this.props.seekId);
    }

	render() {
		return (
			
            <div>
                {this.props.name} ({this.props.cards} pairs) 
                (<a href='#' onClick={this.handleSeekAccept}>accept</a>)
            </div>
		)
	}
}

export default Seek;