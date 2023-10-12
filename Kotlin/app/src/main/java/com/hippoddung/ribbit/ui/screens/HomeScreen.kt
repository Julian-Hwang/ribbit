package com.hippoddung.ribbit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    homeUiState: HomeUiState, modifier: Modifier = Modifier
) {
    when(homeUiState) {
        is HomeUiState.Loading -> LoadingScreen(
            modifier = modifier.fillMaxSize()
        )
        is HomeUiState.Success -> PostsGridScreen(
            homeUiState.posts, modifier
        )
        is HomeUiState.Error -> ErrorScreen(
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier){
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier.size(200.dp),
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(id = R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */

@Composable
fun PostsGridScreen(posts: List<RibbitPost>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier){
        items(posts){post ->
            TextCard(
                post = post,
                modifier = modifier.padding(8.dp))
        }
    }
}

@Composable
fun TextCard(post: RibbitPost, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "@" + post.content,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = post.content,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = post.content,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}