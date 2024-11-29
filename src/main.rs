use std::sync::Arc;
use std::time::Duration;
use axum::http::{HeaderValue, Method};
use axum::http::header::CONTENT_TYPE;
use axum::Router;
use axum::routing::get;
use chrono::Utc;
use dotenvy::dotenv;
use rand::prelude::IndexedRandom;
use rand::thread_rng;
use serde::Serialize;
use tokio::select;
use tokio::sync::RwLock;
use tokio_util::sync::CancellationToken;
use tower_http::cors::CorsLayer;
use tower_http::trace::{DefaultMakeSpan, TraceLayer};
use tracing::{info, subscriber, Level};

const PORT: i32 = 8080;

#[derive(Clone)]
pub struct AppState {
    send_timing_seconds: u64,
}

#[derive(Serialize)]
pub struct RealTimeIncident {
    pub incident_type: String,
    pub location: String,
    pub lat: f64,
    pub long: f64,
    pub description: String,
    pub created_at: String,
}

pub struct Location {
    pub lat: f64,
    pub long: f64,
    location: String,
}

#[tokio::main]
async fn main() {
    dotenv().ok();
    
    let token = CancellationToken::new();
    let real_time_data_sender_thread_token = token.clone();
    
    let app_state = Arc::new(RwLock::new(AppState {
        send_timing_seconds: 15,
    }));
    
    let state_for_real_time_data_sender_thread = Arc::clone(&app_state);
    
    tokio::spawn(async move {
        loop {
            let send_interval_secs = {
                let state = state_for_real_time_data_sender_thread.read().await;
                state.send_timing_seconds
            };
            
            send_real_time_incident_information().await;
            
            select! {
                _ = real_time_data_sender_thread_token.cancelled() => {
                    break;
                }
                _ = tokio::time::sleep(Duration::from_secs(send_interval_secs)) => {
                    info!("Sending completed, sleeping for {} seconds.", send_interval_secs);
                }
            }
        }
    });
    
    let subscriber = tracing_subscriber::fmt()
        .json()
        .with_max_level(Level::INFO)
        .with_current_span(false)
        .finish();
    subscriber::set_global_default(subscriber).expect("Tracing subscriber couldn't be loaded");

    let app = Router::new()
        .route("/api/manage/health", get(|| async { r#"{"status": "UP"}"# }))
        .layer(
            TraceLayer::new_for_http()
                .make_span_with(DefaultMakeSpan::default().include_headers(true)),
        )
        .layer(
            CorsLayer::new()
                .allow_headers([CONTENT_TYPE])
                .allow_methods([Method::GET, Method::POST])
                .allow_origin("http://localhost:3000".parse::<HeaderValue>().unwrap())
                // .allow_origin("https://api.ase-wayfinding.nl".parse::<HeaderValue>().unwrap())
                .allow_methods([Method::GET]),
        )
        .with_state(app_state);

    let listener = tokio::net::TcpListener::bind(&format!("0.0.0.0:{}", PORT))
        .await
        .unwrap();

    info!("listening on {}", PORT);
    axum::serve(listener, app).await.unwrap();
    token.cancel();
}

async fn send_real_time_incident_information() {
    let url = std::env::var("REAL_TIME_INCIDENT_CONSUMER").expect("REAL_TIME_DATA_SENDER_URL is not set in the environment");

    let street_names: Vec<Location> = vec![
        Location {
            lat: 53.3498,
            long: -6.2603,
            location: "O'Connell Street".to_string(),
        },
        Location {
            lat: 53.3416,
            long: -6.2603,
            location: "Grafton Street".to_string(),
        },
        Location {
            lat: 53.3440,
            long: -6.2675,
            location: "Dame Street".to_string(),
        },
        Location {
            lat: 53.3483,
            long: -6.2630,
            location: "Henry Street".to_string(),
        },
        Location {
            lat: 53.3438,
            long: -6.2565,
            location: "Nassau Street".to_string(),
        },
        Location {
            lat: 53.3369,
            long: -6.2497,
            location: "Baggot Street".to_string(),
        },
        Location {
            lat: 53.3475,
            long: -6.2680,
            location: "Capel Street".to_string(),
        },
        Location {
            lat: 53.3345,
            long: -6.2650,
            location: "Camden Street".to_string(),
        },
        Location {
            lat: 53.3410,
            long: -6.2580,
            location: "Dawson Street".to_string(),
        },
        Location {
            lat: 53.3570,
            long: -6.2610,
            location: "Dorset Street".to_string(),
        },
        Location {
            lat: 53.3340,
            long: -6.2620,
            location: "Harcourt Street".to_string(),
        },
        Location {
            lat: 53.3320,
            long: -6.2530,
            location: "Leeson Street".to_string(),
        },
        Location {
            lat: 53.3390,
            long: -6.2480,
            location: "Merrion Square".to_string(),
        },
        Location {
            lat: 53.3380,
            long: -6.2410,
            location: "Mount Street".to_string(),
        },
        Location {
            lat: 53.3520,
            long: -6.2640,
            location: "Parnell Street".to_string(),
        },
        Location {
            lat: 53.3430,
            long: -6.2500,
            location: "Pearse Street".to_string(),
        },
        Location {
            lat: 53.3500,
            long: -6.2510,
            location: "Talbot Street".to_string(),
        },
        Location {
            lat: 53.3430,
            long: -6.2800,
            location: "Thomas Street".to_string(),
        },
        Location {
            lat: 53.3390,
            long: -6.2660,
            location: "Aungier Street".to_string(),
        },
        Location {
            lat: 53.3510,
            long: -6.2690,
            location: "Bolton Street".to_string(),
        },
        Location {
            lat: 53.3330,
            long: -6.2750,
            location: "Clanbrassil Street".to_string(),
        },
        Location {
            lat: 53.3530,
            long: -6.2550,
            location: "Gardiner Street".to_string(),
        },
        Location {
            lat: 53.3430,
            long: -6.2640,
            location: "George's Street".to_string(),
        },
        Location {
            lat: 53.3410,
            long: -6.2540,
            location: "Kildare Street".to_string(),
        },
        Location {
            lat: 53.3500,
            long: -6.2570,
            location: "Marlborough Street".to_string(),
        },
    ];
    
    let descriptions = vec!["A car accident on the highway", "A car accident on the street", "A car accident on the motorway", "A car accident on the road", "A car accident on the freeway"];
    
    let random_location = street_names.choose(&mut thread_rng()).unwrap();
    
    let new_incident = RealTimeIncident {
        incident_type: "Accident".to_string(),
        location: (&random_location.location).to_string(),
        lat: random_location.lat,
        long: random_location.long,
        description: descriptions.choose(&mut thread_rng()).unwrap().to_string(),
        created_at: Utc::now().to_string(),
    };
    
    let client = reqwest::Client::new();
    let request = client.post(format!("{url}"));
    
    let response = request.json(&new_incident).send().await;
    
    match response {
        Ok(_) => {
            info!("Successfully sent real-time incident information");
        },
        Err(e) => {
            info!("Failed to send real-time incident information: {}", e);
        }
    }
}