import client from '../client';

export const getSeeks = (cb) => {
    client({method: 'GET', path: '/api/seeks'}).done((response, error) => {
        cb(response, error);
    });	
}

export const getSeek = (cb, id) => {
    client({method: 'GET', path: '/api/seek', params: {id}}).done((response, error) => {
        cb(response, error);
    });	
}

export const getSeeksBySeekerId = (cb, seekerId) => {
    client({method: 'GET', path: '/api/seeksbyseekerid', params: {seekerId}}).done((response, error) => {
        cb(response, error);
    });	
}

export const deleteSeeksBySeekerId = (cb, seekerId) => {
    client({method: 'DELETE', path: '/api/seeksbyseekerid', params: {seekerId}}).done((response, error) => {
        cb(response, error);
    });	
}

export const postSeek = (cb, playerId, cardPairCount) => {
    client({method: 'POST', path: '/api/seek', params: {playerId, cardPairCount}}).done((response, error) => {		
        cb(response, error);
    });
}

export const postAccept = (cb, seekId, playerId) => {
    client({method: 'POST', path: '/api/accept', params: {seekId, playerId}}).done((response, error) => {
        cb(response, error);
    });
}

export const getGame = (cb, id) => {
    client({method: 'GET', path: '/api/game', params: {id}}).done((response, error) => {
        cb(response, error);
    });	
}

export const postMove = (cb, gameId, playerId, cardIndex) => {
    client({method: 'POST', path: '/api/move', params: {gameId, playerId, cardIndex}}).done((response, error) => {
        cb(response, error);
    });	
}

export const completeTurn = (cb, gameId) => {
    client({method: 'POST', path: '/api/completeTurn', params: {gameId}}).done((response, error) => {
        cb(response, error);
    });	   
}

export const getPlayersByName = (cb, name) => {
    client({method: 'GET', path: '/api/playersbyname', params: {name}}).done((response, error) => {
        cb(response, error);
    });	
}

export const deleteSeek = (cb, id) => {
    client({method: 'DELETE', path: '/api/seek', params: {id}}).done((response, error) => {
        cb(response, error);
    });	      
}

export const postPlayer = (cb, name) => {
    client({method: 'POST', path: '/api/player', params: {name}}).done((response , error) => {
        cb(response, error);
    });
}
